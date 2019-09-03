/*********************************************************************************************
 *
 * 'GamlDocumentationProvider.java, in plugin gama.ui.base.modeling, is part of the source code of the GAMA modeling and
 * simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 *
 *
 **********************************************************************************************/
package ummisco.gama.ui.hover;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.documentation.impl.MultiLineCommentDocumentationProvider;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.google.inject.Inject;

import gama.common.interfaces.IDocManager;
import gama.common.interfaces.IGamlDescription;
import gama.common.util.FileUtils;
import gama.core.lang.gaml.EGaml;
import gama.core.lang.gaml.resource.GamlResourceServices;
import gama.core.lang.scoping.BuiltinGlobalScopeProvider;
import gama.extensions.files.metadata.FileMetaDataProvider;
import gama.util.file.IGamaFileMetaData;
import gaml.compilation.factories.DescriptionFactory;
import gaml.descriptions.FacetProto;
import gaml.descriptions.SymbolProto;
import gaml.expressions.UnitConstantExpression;
import gaml.operators.IUnits;
import gaml.operators.Strings;
import gama.core.lang.gaml.ActionRef;
import gama.core.lang.gaml.Facet;
import gama.core.lang.gaml.Function;
import gama.core.lang.gaml.Import;
import gama.core.lang.gaml.S_Definition;
import gama.core.lang.gaml.S_Do;
import gama.core.lang.gaml.S_Global;
import gama.core.lang.gaml.Statement;
import gama.core.lang.gaml.StringLiteral;
import gama.core.lang.gaml.TypeRef;
import gama.core.lang.gaml.UnitName;
import gama.core.lang.gaml.VarDefinition;
import gama.core.lang.gaml.VariableRef;
import ummisco.gama.ui.editor.GamlHyperlinkDetector;

public class GamlDocumentationProvider extends MultiLineCommentDocumentationProvider {

	@Inject protected GamlHyperlinkDetector detector;

	public String getOnlyComment(final EObject o) {
		return super.getDocumentation(o);
	}

	@Override
	public String getDocumentation(final EObject o) {
		if (o instanceof Import)
			return "ctrl-click or cmd-click on the path to open this model in a new editor";
		if (o instanceof S_Global)
			return getDocumentation(o.eContainer().eContainer());
		if (o instanceof StringLiteral) {
			final URI iu = detector.getURI((StringLiteral) o);
			if (iu != null) {
				if (FileUtils.isFileExistingInWorkspace(iu)) {
					final IFile file = FileUtils.getWorkspaceFile(iu);
					final IGamaFileMetaData data = FileMetaDataProvider.getInstance().getMetaData(file, false, true);
					if (data != null) {
						String s = data.getDocumentation();
						if (s != null) {
							s = s.replace(Strings.LN, "<br/>");
							return s;
						}
					} else {
						final String ext = file.getFileExtension();
						return "This workspace " + ext + " file has no metadata associated with it";
					}
				} else { // absolute file
					final IFile file =
							FileUtils.createLinkToExternalFile(((StringLiteral) o).getOp(), o.eResource().getURI());
					if (file == null)
						return "This file is outside the workspace and cannot be found.";
					final IGamaFileMetaData data = FileMetaDataProvider.getInstance().getMetaData(file, false, true);
					if (data != null) {
						String s = data.getDocumentation();
						if (s != null) {
							s = s.replace(Strings.LN, "<br/>");
							return s;
						}
					} else {
						final String ext = file.getFileExtension();
						return "This external " + ext + " file has no metadata associated with it";
					}
				}
			}
		}

		String comment = super.getDocumentation(o);
		if (comment == null) {
			comment = "";
		}
		if (o instanceof VariableRef) {
			comment = super.getDocumentation(((VariableRef) o).getRef());
		} else if (o instanceof ActionRef) {
			comment = super.getDocumentation(((ActionRef) o).getRef());
		}
		if (comment == null) {
			comment = "";
		} else {
			comment += "<br/>";
		}
		if (o instanceof TypeRef) {
			final Statement s = EGaml.getInstance().getStatement(o);
			if (s instanceof S_Definition && ((S_Definition) s).getTkey() == o) {
				final IDocManager dm = GamlResourceServices.getResourceDocumenter();
				final IGamlDescription gd = dm.getGamlDocumentation(s);
				if (gd != null)
					return gd.getDocumentation();
			}
		} else if (o instanceof Function) {
			final Function f = (Function) o;
			if (f.getLeft() instanceof ActionRef) {
				final ActionRef ref = (ActionRef) f.getLeft();
				final String temp = getDocumentation(ref.getRef());
				if (!temp.contains("No documentation"))
					return temp;
			}
		} else if (o instanceof VariableRef) {
			// Case of do xxx;
			if (o.eContainer() instanceof S_Do && ((S_Do) o.eContainer()).getExpr() == o) {
				VarDefinition vd = ((VariableRef) o).getRef();
				final IGamlDescription description =
						GamlResourceServices.getResourceDocumenter().getGamlDocumentation(vd);
				if (description != null) {
					String result = description.getDocumentation();
					if (result == null)
						return "";
					return result;
				}
			}
			final VarDefinition vd = ((VariableRef) o).getRef();
			if (vd != null) {
				if (vd.eContainer() == null) {
					final IEObjectDescription desc = BuiltinGlobalScopeProvider.getVar(vd.getName());
					if (desc != null) {
						String userData = desc.getUserData("doc");
						if (userData != null && !userData.isEmpty())
							return userData;
					}
				}
			}
		} else if (o instanceof UnitName) {
			final String name = ((UnitName) o).getRef().getName();
			final UnitConstantExpression exp = IUnits.UNITS_EXPR.get(name);
			if (exp != null)
				return exp.getDocumentation();
		}

		final IGamlDescription description = GamlResourceServices.getResourceDocumenter().getGamlDocumentation(o);

		// TODO Add a swtich for constants

		if (description == null) {
			if (o instanceof Facet) {
				String facetName = ((Facet) o).getKey();
				facetName = facetName.substring(0, facetName.length() - 1);
				final EObject cont = o.eContainer();
				final String key = EGaml.getInstance().getKeyOf(cont);
				final SymbolProto p = DescriptionFactory.getProto(key, null);
				if (p != null) {
					final FacetProto f = p.getPossibleFacets().get(facetName);
					if (f != null)
						return comment + Strings.LN + f.getDocumentation();
				}
				return comment;
			}
			if (comment.isEmpty())
				return null;
			return comment + Strings.LN + "No documentation.";
		}

		return comment + description.getDocumentation();
	}

}