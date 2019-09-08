/*******************************************************************************************************
 *
 * gaml.compilation.ast.SyntacticSpeciesElement.java, in plugin gama.core,
 * is part of the source code of the GAMA modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.core.lang.gaml.ast;

import org.eclipse.emf.ecore.EObject;

import gaml.compilation.interfaces.ISyntacticElement;
import gaml.compilation.interfaces.ISyntacticElement.SyntacticVisitor;
import gaml.statements.Facets;

/**
 * Class GlobalSyntacticElement.
 * 
 * @author drogoul
 * @since 9 sept. 2013
 * 
 */
public class SyntacticSpeciesElement extends SyntacticStructuralElement {

	/**
	 * Instantiates a new syntactic species element.
	 *
	 * @param keyword the keyword
	 * @param facets the facets
	 * @param statement the statement
	 */
	SyntacticSpeciesElement(final String keyword, final Facets facets, final EObject statement) {
		super(keyword, facets, statement);
	}

	/* (non-Javadoc)
	 * @see gaml.compilation.ast.AbstractSyntacticElement#visitSpecies(gaml.compilation.ast.ISyntacticElement.SyntacticVisitor)
	 */
	@Override
	public void visitSpecies(final SyntacticVisitor visitor) {
		visitAllChildren(visitor, SPECIES_FILTER);
	}

	/* (non-Javadoc)
	 * @see gaml.compilation.ast.AbstractSyntacticElement#isSpecies()
	 */
	@Override
	public boolean isSpecies() {
		return true;
	}

}