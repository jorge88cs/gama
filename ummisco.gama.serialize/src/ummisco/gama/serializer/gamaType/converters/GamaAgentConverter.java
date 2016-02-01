/*********************************************************************************************
 * 
 * 
 * 'GamaAgentConverter.java', in plugin 'ummisco.gama.communicator', is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2014 UMI 209 UMMISCO IRD/UPMC & Partners
 * 
 * Visit https://code.google.com/p/gama-platform/ for license information and developers contact.
 * 
 * 
 **********************************************************************************************/
package ummisco.gama.serializer.gamaType.converters;

import msi.gama.kernel.experiment.ExperimentAgent;
import msi.gama.metamodel.agent.GamlAgent;
import msi.gama.metamodel.agent.IAgent;
import msi.gama.runtime.IScope;

import java.util.List;

import com.thoughtworks.xstream.converters.*;
import com.thoughtworks.xstream.io.*;

public class GamaAgentConverter implements Converter {

	IScope scope;
	
	public GamaAgentConverter(IScope s){
		scope = s;
	}
	
	@Override
	public boolean canConvert(final Class arg0) {
		return (arg0.equals(GamlAgent.class));
	}

	@Override
	public void marshal(final Object arg0, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		GamlAgent agt = (GamlAgent) arg0;
		
		writer.startNode("agentReference");
		System.out.println("ConvertAnother : AgentConverter " + agt.getClass());
	//	System.out.println("" + agt.getName() + " - " + agt.getSpeciesName());
	// 	context.convertAnother(new RemoteAgent(agt));
		writer.setValue(agt.getName());
		System.out.println("===========END ConvertAnother : GamaAgent");
		
		writer.endNode();
	}

	@Override
	public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext arg1) {

		reader.moveDown();
		// RemoteAgent rmt = (RemoteAgent) arg1.convertAnother(null, RemoteAgent.class);
		List<IAgent> lagt = scope.getSimulationScope().getAgents(scope);
		boolean found = false;
		int i = 0;
		IAgent agt = null;
		while(!found && (i < lagt.size())) {
			if(lagt.get(i).getName().equals(reader.getValue())) {
				found = true;
				agt = lagt.get(i);
			}
		}
		reader.moveUp();
		return agt;
	}

}