/**-
* Name: City Evacuation
* Author: Mathieu Bourgais & Patrick Taillandier
* Description:  Example model concerning the  use of the simpleBDI plug-in  with emotions. 
* A technological accident is simulated in one of the buildings of the city center.

* Tag : simple_bdi, emotion, evacuation
*/

model City_Evacuation

global {
	file shapefile_roads <- file("../includes/Rouen roads.shp");
	file shapefile_hazard <- file("../includes/Technological hazard.shp");
	file shapefile_shelters <- file("../includes/Escapes.shp");
	geometry shape <- envelope(shapefile_roads);
	graph road_network;
	map<road,float> current_weights;
	
	float hazard_distance <- 400.0;
	float catastrophe_distance <- 100.0;
	float proba_detect_hazard <- 0.2;
	float proba_detect_other_escape <- 0.01;
	float other_distance <- 10.0;
	
	init {
		create road from: shapefile_roads;
		create hazard from: shapefile_hazard;
		create catastrophe;
		create shelter from: shapefile_shelters;
		
		//at the begining of the simulation, we add to the people agent the desire to go to their target.
		create people number: 200{
			location <- any_location_in(one_of(road));
			do add_desire(at_target);
			
		 	//the agent has also the desire that there is no catastrophe (we set the piority of this desire to 0 as it is a general desire)
			do add_desire(new_predicate("catastrophe",false) with_priority 0.0);
			
			// we give the agent a random charisma and receptivity (built-in variables linked to the emotions)
			charisma<-rnd(1.0);
			receptivity<-rnd(1.0);
      	}
      	road_network <- as_edge_graph(road);
      	current_weights <- road as_map (each::each.shape.perimeter);
	}
	
	reflex update_speeds when: every(10){
		current_weights <- road as_map (each::each.shape.perimeter / each.speed_coeff);
		road_network <- road_network with_weights current_weights;
	}
	
	reflex stop_sim when: empty(people) {
		do pause;
	}
}
 
species people skills: [moving] control: simple_bdi{
	point target;
	float speed <- 30 #km/#h;
	rgb color <- #blue;
	bool escape_mode <- false;
	
	//in order to simplify the model we define  4 desires as variables
	predicate at_target <- new_predicate("at_target") with_priority 1;
	predicate in_shelter <- new_predicate("shelter") with_priority 5;
	predicate has_target <- new_predicate("has target") with_priority 2;
	predicate has_shelter <- new_predicate("has shelter") with_priority 10;

    //we give them as well 2 beliefs as variables
	predicate catastropheP <- new_predicate("catastrophe");
	predicate nonCatastrophe <- new_predicate("catastrophe",false) with_priority 0.0;
	
	//at last we define 2 emotion linked to the knowledge of the catastrophe
	emotion fearConfirmed <- new_emotion("fear_confirmed",catastropheP);
	emotion fear <- new_emotion("fear",catastropheP);
	
	bool noTarget<-true;
	
	//we set this built-in variable to true to use the emotional process
	bool use_emotions_architecture <- true;

    //if the agent perceive that their is something that is not normal (a hazard), it has a probability proba_detect_hazard to suppose (add to its unertainty base) that there is a catastrophe occuring
	perceive target:hazard in: hazard_distance when: not escape_mode and flip(proba_detect_hazard){
		ask myself {
			do add_uncertainty(catastropheP);
			color<-#green;
		}
	}

	//if the agent perceive the catastrophe, it adds a belief about it and pass in escape mode
	perceive target:catastrophe in:catastrophe_distance{
		ask myself{
			do add_belief(catastropheP);
			if(not escape_mode){
				do to_escape_mode;
			}
		}
	}

	//if the agent perceives other people agents in their neighborhood that have fear, it can be contaminate by this emotion
	perceive target:people in: other_distance when: not escape_mode {
		unconscious_contagion emotion:fearConfirmed when: has_emotion(fear);
		unconscious_contagion emotion:new_emotion("fear") charisma: charisma receptivity:receptivity;
		conscious_contagion emotion_detected:fearConfirmed emotion_created:fear;
	}
	
	//if the agent has a fear confirmed, it has the desire to go to a shelter
	rule emotion:fearConfirmed remove_intention: at_target new_desire:in_shelter;
	
	//if the agent has the belief that there is a a catastrophe,  it has the desire to go to a shelter
	rule belief:catastropheP remove_intention:at_target new_desire:in_shelter;
	
	//normal move plan
	plan normal_move intention: at_target  {
		if (target = nil) {
			target <- any_location_in(one_of(road));
		} else {
			do goto target: target on: road_network move_weights: current_weights recompute_path: false;
			if (target = location)  {
				target <- nil;
				noTarget<-true;
			}
		}
	}
	
	//fast evacuation plan in case where the agent has a fear confirmed
	plan evacuationFast intention: in_shelter emotion: fearConfirmed priority:2 {
		color <- #yellow;
		speed <- 60 #km/#h;
		if (target = nil or noTarget) {
			target <- (shelter with_min_of (each.location distance_to location)).location;
			noTarget <- false;
		}
		else  {
			do goto target: target on: road_network move_weights: current_weights recompute_path: false;
			if (target = location)  {
				do die;
			}		
		}
	}	
	
	//normal evacuation plan
	plan evacuation intention: in_shelter {
		color <-#darkred;
		if (target = nil or noTarget) {
			target <- (shelter with_min_of (each.location distance_to location)).location;
			noTarget <- false;
		}
		else  {
			do goto target: target on: road_network move_weights: current_weights recompute_path: false;
			if (target = location)  {
				do die;
			}		
		}
	}
	
	action to_escape_mode {
		escape_mode <- true;
		color <- #darkred;
		target <- nil;	
		noTarget <- true;
		do remove_intention(at_target, true);
	}
	
	
	aspect default {
		draw triangle(20) rotate: heading + 90 color: color;
	}
}

species road {
	float capacity <- 1 + shape.perimeter/50;
	int nb_people <- 0 update: length(people at_distance 1);
	float speed_coeff <- 1.0 update:  exp(-nb_people/capacity) min: 0.1;
	
	aspect default {
		draw shape color: #black;
	}
}

species shelter {
	aspect default {
		draw circle(30) color: #magenta border: #black;
	}
}

species hazard {
	aspect default {
		draw circle(hazard_distance) empty: true color: #red depth:1;
	}
}

species catastrophe{
	init{
		location <- first(hazard).location;
	}
	aspect default{
		draw circle(catastrophe_distance) empty: true color: #darkgreen depth:2;
	}
}

experiment main type: gui {
	output {
		display map type: opengl{
			species shelter refresh: false;
			species catastrophe refresh: false;
			species hazard refresh: false;
			species road refresh: false;
			species people;
		}
	}
}