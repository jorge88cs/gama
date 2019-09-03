/*********************************************************************************************
 *
 * 'AdamsBashforthSolver.java, in plugin gama.extensions.maths, is part of the source code of the GAMA modeling
 * and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 *
 *
 **********************************************************************************************/
package gama.extensions.maths.ode.utils.solver;

import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;

import gama.util.list.IList;
import gama.util.map.IMap;

public class AdamsBashforthSolver extends Solver {

	public AdamsBashforthSolver(final int nSteps, final double minStep, final double maxStep,
			final double scalAbsoluteTolerance, final double scalRelativeTolerance,
			final IMap<String, IList<Double>> integrated_val) {
		super((minStep + maxStep) / 2,
				new AdamsBashforthIntegrator(nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance),
				integrated_val);
	}

}