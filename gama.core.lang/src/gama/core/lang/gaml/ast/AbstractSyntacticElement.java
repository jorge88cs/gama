/*******************************************************************************************************
 *
 * gaml.compilation.ast.AbstractSyntacticElement.java, in plugin gama.core, is part of the source code of the
 * GAMA modeling and simulation platform (v. 1.8)
 *
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 *
 ********************************************************************************************************/
package gama.core.lang.gaml.ast;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import gama.common.interfaces.IKeyword;
import gaml.compilation.interfaces.ISyntacticElement;
import gaml.compilation.interfaces.ISyntacticElement.SyntacticVisitor;
import gaml.descriptions.IExpressionDescription;
import gaml.descriptions.IDescription.IFacetVisitor;
import gaml.prototypes.SymbolProto;
import gaml.statements.Facets;

/**
 * Class AbstractSyntacticElement.
 *
 * @author drogoul
 * @since 15 sept. 2013
 *
 */
public abstract class AbstractSyntacticElement implements ISyntacticElement {

	/**
	 * The facets.
	 */
	private Facets facets;

	/**
	 * The keyword.
	 */
	private String keyword;

	/**
	 * The element.
	 */
	final EObject element;

	/**
	 * Instantiates a new abstract syntactic element.
	 *
	 * @param keyword
	 *            the keyword
	 * @param facets
	 *            the facets
	 * @param element
	 *            the element
	 */
	AbstractSyntacticElement(final String keyword, final Facets facets, final EObject element) {
		this.keyword = keyword;
		this.facets = facets;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#getElement()
	 */
	@Override
	public EObject getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKeyword() + " " + getName() + " " + (facets == null ? "" : facets.getFacets().toString());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#addChild(gaml.compilation.ast.ISyntacticElement)
	 */
	@Override
	public void addChild(final ISyntacticElement e) {
		throw new RuntimeException("No children allowed for " + getKeyword());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#setKeyword(java.lang.String)
	 */
	@Override
	public void setKeyword(final String name) {
		keyword = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#getKeyword()
	 */
	@Override
	public String getKeyword() {
		return keyword;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#hasFacets()
	 */
	@Override
	public final boolean hasFacets() {
		return facets != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#hasFacet(java.lang.String)
	 */
	@Override
	public final boolean hasFacet(final String name) {
		return facets != null && facets.containsKey(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#getExpressionAt(java.lang.String)
	 */
	@Override
	public final IExpressionDescription getExpressionAt(final String name) {
		return facets == null ? null : facets.get(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#copyFacets(gaml.descriptions.SymbolProto)
	 */
	@Override
	public final Facets copyFacets(final SymbolProto sp) {
		if (facets != null) {
			final Facets ff = new Facets();
			visitFacets((a, b) -> {
				if (b != null) {
					ff.put(a, sp != null && sp.isLabel(a) ? b.cleanCopy().compileAsLabel() : b.cleanCopy());
				}
				return true;
			});
			return ff;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#setFacet(java.lang.String,
	 * gaml.descriptions.IExpressionDescription)
	 */
	@Override
	public void setFacet(final String string, final IExpressionDescription expr) {
		if (expr == null) { return; }
		if (facets == null) {
			facets = new Facets();
		}
		facets.put(string, expr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#getName()
	 */
	@Override
	public String getName() {
		// Default behavior. Redefined in subclasses
		final IExpressionDescription expr = getExpressionAt(IKeyword.NAME);
		return expr == null ? null : expr.toString();
	}

	/**
	 * Removes the facet.
	 *
	 * @param name
	 *            the name
	 */
	protected void removeFacet(final String name) {
		if (facets == null) { return; }
		facets.remove(name);
		if (facets.isEmpty()) {
			facets = null;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#isSpecies()
	 */
	@Override
	public boolean isSpecies() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#isExperiment()
	 */
	@Override
	public boolean isExperiment() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#computeStats(java.util.Map)
	 */
	@Override
	public void computeStats(final Map<String, Integer> stats) {
		final String s = getClass().getSimpleName();
		if (!stats.containsKey(s)) {
			stats.put(s, 1);
		} else {
			stats.put(s, stats.get(s) + 1);
		}
		visitAllChildren(element -> element.computeStats(stats));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitFacets(gaml.descriptions.IDescription.IFacetVisitor)
	 */
	@Override
	public void visitFacets(final IFacetVisitor visitor) {
		if (facets == null) { return; }
		facets.forEachFacet(visitor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#compact()
	 */
	@Override
	public void compact() {
		if (facets == null) { return; }
		if (facets.isEmpty()) {
			facets.dispose();
			facets = null;
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitThisAndAllChildrenRecursively(gaml.compilation.ast.
	 * ISyntacticElement.SyntacticVisitor)
	 */
	@Override
	public void visitThisAndAllChildrenRecursively(final SyntacticVisitor visitor) {
		visitor.visit(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitChildren(gaml.compilation.ast.ISyntacticElement.
	 * SyntacticVisitor)
	 */
	@Override
	public void visitChildren(final SyntacticVisitor visitor) {}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitSpecies(gaml.compilation.ast.ISyntacticElement.
	 * SyntacticVisitor)
	 */
	@Override
	public void visitSpecies(final SyntacticVisitor visitor) {}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitExperiments(gaml.compilation.ast.ISyntacticElement.
	 * SyntacticVisitor)
	 */
	@Override
	public void visitExperiments(final SyntacticVisitor visitor) {}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitGrids(gaml.compilation.ast.ISyntacticElement.
	 * SyntacticVisitor)
	 */
	@Override
	public void visitGrids(final SyntacticVisitor visitor) {}

	/*
	 * (non-Javadoc)
	 *
	 * @see gaml.compilation.ast.ISyntacticElement#visitAllChildren(gaml.compilation.ast.ISyntacticElement.
	 * SyntacticVisitor)
	 */
	@Override
	public void visitAllChildren(final SyntacticVisitor visitor) {
		visitGrids(visitor);
		visitSpecies(visitor);
		visitChildren(visitor);
		// visitExperiments(visitor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gama.common.interfaces.IDisposable#dispose()
	 */
	@Override
	public void dispose() {
		if (facets != null) {
			facets.dispose();
		}
	}

}