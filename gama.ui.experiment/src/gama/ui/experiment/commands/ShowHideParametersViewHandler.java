/*********************************************************************************************
 *
 * 'ShowHideParametersViewHandler.java, in plugin gama.ui.experiment.experiment, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package gama.ui.experiment.commands;

import org.eclipse.core.commands.*;
import gama.runtime.GAMA;

public class ShowHideParametersViewHandler extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		GAMA.getGui().showParameterView(null, GAMA.getExperiment());
		return null;
	}
}
