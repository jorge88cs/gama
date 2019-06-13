/*
 * Stop.java
 *
 *
 * The Salamander Project - 2D and 3D graphics libraries in Java Copyright (C) 2004 Mark McKay
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Mark McKay can be contacted at mark@kitfox.com. Salamander and other projects can be found at http://www.kitfox.com
 *
 * Created on January 26, 2004, 1:56 AM
 */

package msi.gama.util.file.svg;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Symbol extends Group {
	AffineTransform viewXform;
	Rectangle2D viewBox;

	/** Creates a new instance of Stop */
	public Symbol() {}
	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) { //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * String viewBoxStrn = attrs.getValue("viewBox"); if (viewBoxStrn != null) { float[] dim =
	 * XMLParseUtil.parseFloatList(viewBoxStrn); viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]); } }
	 */
	/*
	 * public void loaderEndElement(SVGLoaderHelper helper) { if (viewBox == null) { viewBox = super.getBoundingBox(); }
	 *
	 * //Transform pattern onto unit square viewXform = new AffineTransform(); viewXform.scale(1.0 / viewBox.getWidth(),
	 * 1.0 / viewBox.getHeight()); viewXform.translate(-viewBox.getX(), -viewBox.getY()); }
	 */

	@Override
	protected void build() throws SVGException {
		super.build();

		final StyleAttribute sty = new StyleAttribute();

		// sty = getPres("unicode");
		// if (sty != null) unicode = sty.getStringValue();

		if (getPres(sty.setName("viewBox"))) {
			final float[] dim = sty.getFloatList();
			viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
		}

		if (viewBox == null) {
			// viewBox = super.getBoundingBox();
			viewBox = new Rectangle(0, 0, 1, 1);
		}

		// Transform pattern onto unit square
		viewXform = new AffineTransform();
		viewXform.scale(1.0 / viewBox.getWidth(), 1.0 / viewBox.getHeight());
		viewXform.translate(-viewBox.getX(), -viewBox.getY());
	}

	@Override
	protected boolean outsideClip(final Graphics2D g) throws SVGException {
		g.getClipBounds(clipBounds);
		final Rectangle2D rect = super.getBoundingBox();
		return !rect.intersects(clipBounds);
	}

	@Override
	public void render(final Graphics2D g) throws SVGException {
		final AffineTransform oldXform = g.getTransform();
		g.transform(viewXform);

		super.render(g);

		g.setTransform(oldXform);
	}

	@Override
	public Shape getShape() {
		final Shape shape = super.getShape();
		return viewXform.createTransformedShape(shape);
	}

	@Override
	public Rectangle2D getBoundingBox() throws SVGException {
		final Rectangle2D rect = super.getBoundingBox();
		return viewXform.createTransformedShape(rect).getBounds2D();
	}

	/**
	 * Updates all attributes in this diagram associated with a time event. Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time update
	 */
	// @Override
	// public boolean updateTime(final double curTime) throws SVGException {
	// // if (trackManager.getNumTracks() == 0) return false;
	// final boolean changeState = super.updateTime(curTime);
	//
	// // View box properties do not change
	//
	// return changeState;
	// }

}
