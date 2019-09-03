/*******************************************************************************************************
 *
 * gama.core.outputs.layers.GraphicLayerStatement.java, in plugin msi.gama.core, is part of the source code of the GAMA
 * modeling and simulation platform (v. 1.8)
 *
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.core.outputs.layers;

import gama.processor.annotations.IConcept;
import gama.processor.annotations.ISymbolKind;
import gama.processor.annotations.GamlAnnotations.doc;
import gama.processor.annotations.GamlAnnotations.example;
import gama.processor.annotations.GamlAnnotations.facet;
import gama.processor.annotations.GamlAnnotations.facets;
import gama.processor.annotations.GamlAnnotations.inside;
import gama.processor.annotations.GamlAnnotations.symbol;
import gama.processor.annotations.GamlAnnotations.usage;
import gama.common.interfaces.IKeyword;
import gama.runtime.exceptions.GamaRuntimeException;
import gama.runtime.scope.IScope;
import gaml.compilation.factories.DescriptionFactory;
import gaml.compilation.interfaces.ISymbol;
import gaml.descriptions.IDescription;
import gaml.statements.AspectStatement;
import gaml.types.IType;

@symbol (
		name = "graphics",
		kind = ISymbolKind.LAYER,
		with_sequence = true,
		concept = { IConcept.DISPLAY })
@inside (
		symbols = IKeyword.DISPLAY)
@facets (
		value = { @facet (
				name = IKeyword.POSITION,
				type = IType.POINT,
				optional = true,
				doc = @doc ("position of the upper-left corner of the layer. Note that if coordinates are in [0,1[, the position is relative to the size of the environment (e.g. {0.5,0.5} refers to the middle of the display) whereas it is absolute when coordinates are greater than 1 for x and y. The z-ordinate can only be defined between 0 and 1. The position can only be a 3D point {0.5, 0.5, 0.5}, the last coordinate specifying the elevation of the layer.")),
				@facet (
						name = IKeyword.TRACE,
						type = { IType.BOOL, IType.INT },
						optional = true,
						doc = @doc ("Allows to aggregate the visualization at each timestep on the display. Default is false. If set to an int value, only the last n-th steps will be visualized. If set to true, no limit of timesteps is applied. ")),
				@facet (
						name = IKeyword.FADING,
						type = { IType.BOOL },
						optional = true,
						doc = @doc ("Used in conjunction with 'trace:', allows to apply a fading effect to the previous traces. Default is false")),
				@facet (
						name = IKeyword.SIZE,
						type = IType.POINT,
						optional = true,
						doc = @doc ("extent of the layer in the screen from its position. Coordinates in [0,1[ are treated as percentages of the total surface, while coordinates > 1 are treated as absolute sizes in model units (i.e. considering the model occupies the entire view). Like in 'position', an elevation can be provided with the z coordinate, allowing to scale the layer in the 3 directions ")),
				@facet (
						name = IKeyword.TRANSPARENCY,
						type = IType.FLOAT,
						optional = true,
						doc = @doc ("the transparency rate of the agents (between 0 and 1, 1 means no transparency)")),
				@facet (
						name = IKeyword.NAME,
						type = IType.LABEL,
						optional = true,
						doc = @doc ("the human readable title of the graphics")),
				@facet (
						name = IKeyword.REFRESH,
						type = IType.BOOL,
						optional = true,
						doc = @doc ("(openGL only) specify whether the display of the species is refreshed. (true by default, usefull in case of agents that do not move)")) },
		omissible = IKeyword.NAME)
@doc (
		value = "`graphics` allows the modeler to freely draw shapes/geometries/texts without having to define a species. It works exactly like a species [Aspect161 aspect]: the draw statement can be used in the same way.",
		usages = { @usage (
				value = "The general syntax is:",
				examples = { @example (
						value = "display my_display {",
						isExecutable = false),
						@example (
								value = "   graphics \"my new layer\" {",
								isExecutable = false),
						@example (
								value = "      draw circle(5) at: {10,10} color: #red;",
								isExecutable = false),
						@example (
								value = "      draw \"test\" at: {10,10} size: 20 color: #black;",
								isExecutable = false),
						@example (
								value = "   }",
								isExecutable = false),
						@example (
								value = "}",
								isExecutable = false) }) },
		see = { IKeyword.DISPLAY, IKeyword.AGENTS, IKeyword.CHART, IKeyword.EVENT, "graphics", IKeyword.GRID_POPULATION,
				IKeyword.IMAGE, IKeyword.OVERLAY, IKeyword.POPULATION })
public class GraphicLayerStatement extends AbstractLayerStatement {

	AspectStatement aspect;
	static int LayerIndex;

	public GraphicLayerStatement(final IDescription desc) throws GamaRuntimeException {
		super(desc);
		final IDescription d =
				DescriptionFactory.create(IKeyword.ASPECT, desc, IKeyword.NAME, "graphic_aspect" + LayerIndex++);
		aspect = new AspectStatement(d);
	}

	@Override
	public void setChildren(final Iterable<? extends ISymbol> children) {
		aspect.setChildren(children);
	}

	public AspectStatement getAspect() {
		return aspect;
	}

	@Override
	public LayerType getType(final boolean isOpenGL) {
		return LayerType.GRAPHICS;
	}

	@Override
	protected boolean _init(final IScope scope) {
		return true;
	}

	@Override
	protected boolean _step(final IScope scope) {
		return true;
	}

}
