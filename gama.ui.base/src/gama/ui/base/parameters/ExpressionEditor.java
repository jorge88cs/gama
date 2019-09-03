/*********************************************************************************************
 *
 * 'ExpressionEditor.java, in plugin gama.ui.base.shared, is part of the source code of the
 * GAMA modeling and simulation platform.
 * (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package gama.ui.base.parameters;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import gama.ui.base.interfaces.EditorListener;
import gama.runtime.scope.IScope;
import gaml.expressions.IExpression;
import gaml.types.IType;

public class ExpressionEditor extends GenericEditor<IExpression> {

	private String expressionText;

	ExpressionEditor(final IScope scope, final Composite parent, final String title, final IExpression value,
			final EditorListener<IExpression> whenModified, final IType<?> expectedType) {
		super(scope, parent, title, value, whenModified);
		this.expectedType = expectedType;
	}

	@Override
	public Control createCustomParameterControl(final Composite comp) {
		// if ( currentValue instanceof String ) {
		// expressionText = (String) currentValue;
		// } else if ( currentValue instanceof IExpression ) {
		expressionText = currentValue.serialize(true);
		// }
		return super.createCustomParameterControl(comp);
	}

	@Override
	protected void displayParameterValue() {
		getEditorControl().setText(expressionText);
	}

	@Override
	public IExpression getParameterValue() {
		return (IExpression) param.value(getScope());
	}

	@Override
	protected String typeToDisplay() {
		return "expression";
	}

	@Override
	public boolean evaluateExpression() {
		return false;
	}

	public void setEditorTextNoPopup(final String s) {
		internalModification = true;
		getEditorControl().setText(s);
		internalModification = false;
	}

}
