/*********************************************************************************************
 *
 * 'StepByStepHandler.java, in plugin gama.ui.experiment.experiment, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package gama.ui.experiment.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;

import gama.ui.base.bindings.GamaKeyBindings;
import gama.runtime.GAMA;

public class StepByStepHandler extends AbstractHandler implements IElementUpdater {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		GAMA.stepFrontmostExperiment();
		return this;
	}

	@Override
	public void updateElement(final UIElement element, final Map parameters) {
		element.setTooltip("Runs the experiment one step at a time (" + GamaKeyBindings.STEP_STRING + ")");
		element.setText("Step Experiment (" + GamaKeyBindings.STEP_STRING + ")");
	}

}
