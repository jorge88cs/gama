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

import java.awt.Shape;
import java.awt.geom.Area;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class ClipPath extends SVGElement {

	public static final int CP_USER_SPACE_ON_USE = 0;
	public static final int CP_OBJECT_BOUNDING_BOX = 1;

	int clipPathUnits = CP_USER_SPACE_ON_USE;

	/** Creates a new instance of Stop */
	public ClipPath() {}

	@Override
	protected void build() throws SVGException {
		super.build();

		final StyleAttribute sty = new StyleAttribute();

		clipPathUnits = getPres(sty.setName("clipPathUnits")) && sty.getStringValue().equals("objectBoundingBox")
				? CP_OBJECT_BOUNDING_BOX : CP_USER_SPACE_ON_USE;
	}

	public int getClipPathUnits() {
		return clipPathUnits;
	}

	public Shape getClipPathShape() {
		if (children.size() == 0) { return null; }
		if (children.size() == 1) { return ((ShapeElement) children.get(0)).getShape(); }

		Area clipArea = null;
		for (final Object element : children) {
			final ShapeElement se = (ShapeElement) element;

			if (clipArea == null) {
				final Shape shape = se.getShape();
				if (shape != null) {
					clipArea = new Area(se.getShape());
				}
				continue;
			}

			final Shape shape = se.getShape();
			if (shape != null) {
				clipArea.intersect(new Area(shape));
			}
		}

		return clipArea;
	}

	/**
	 * Updates all attributes in this diagram associated with a time event. Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time update
	 */
	// @Override
	// public boolean updateTime(final double curTime) throws SVGException {
	// // if (trackManager.getNumTracks() == 0) return false;
	//
	// // Get current values for parameters
	// final StyleAttribute sty = new StyleAttribute();
	// boolean shapeChange = false;
	//
	// if (getPres(sty.setName("clipPathUnits"))) {
	// final String newUnitsStrn = sty.getStringValue();
	// final int newUnits =
	// newUnitsStrn.equals("objectBoundingBox") ? CP_OBJECT_BOUNDING_BOX : CP_USER_SPACE_ON_USE;
	//
	// if (newUnits != clipPathUnits) {
	// clipPathUnits = newUnits;
	// shapeChange = true;
	// }
	// }
	//
	// if (shapeChange) {
	// build();
	// }
	//
	// return shapeChange;
	// }
}
