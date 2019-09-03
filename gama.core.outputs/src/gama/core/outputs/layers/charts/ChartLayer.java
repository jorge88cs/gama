/*******************************************************************************************************
 *
 * gama.core.outputs.layers.charts.ChartLayer.java, in plugin msi.gama.core, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8)
 *
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.core.outputs.layers.charts;

import java.awt.geom.Rectangle2D;

import gama.core.outputs.layers.AbstractLayer;
import gama.common.interfaces.outputs.IDisplaySurface;
import gama.common.interfaces.outputs.IGraphics;
import gama.common.interfaces.outputs.ILayerStatement;
import gama.metamodel.shape.IShape;
import gama.runtime.scope.IScope;

/**
 * Written by drogoul Modified on 1 avr. 2010
 *
 * @todo Description
 *
 */
public class ChartLayer extends AbstractLayer {

	public ChartLayer(final ILayerStatement model) {
		super(model);
	}

	@Override
	public Rectangle2D focusOn(final IShape geometry, final IDisplaySurface s) {
		return null;
	}

	private ChartOutput getChart() {
		return ((ChartLayerStatement) definition).getOutput();
	}

	@Override
	public String getType() {
		return "Chart layer";
	}

	@Override
	public void privateDraw(final IScope scope, final IGraphics dg) {
		dg.drawChart(getChart());
	}

	@Override
	public boolean stayProportional() {
		return false;
	}

	@Override
	public boolean isProvidingWorldCoordinates() {
		return false;
	}

	@Override
	public void getModelCoordinatesInfo(final int xOnScreen, final int yOnScreen, final IDisplaySurface g,
			final StringBuilder sb) {
		getChart().getModelCoordinatesInfo(xOnScreen, yOnScreen, g, getData().getPositionInPixels(), sb);
	}

}
