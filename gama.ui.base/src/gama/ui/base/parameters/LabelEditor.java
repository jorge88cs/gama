/*********************************************************************************************
 *
 * 'LabelEditor.java, in plugin gama.ui.base.shared, is part of the source code of the GAMA modeling and simulation
 * platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 *
 *
 **********************************************************************************************/
package gama.ui.base.parameters;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import gama.ui.base.interfaces.EditorListener;
import gama.common.interfaces.IAgent;
import gama.common.interfaces.experiment.IParameter;
import gama.runtime.scope.IScope;

public class LabelEditor extends AbstractEditor<String> {

	private Text textBox;

	LabelEditor(final IScope scope, final IAgent agent, final IParameter param, final EditorListener<String> l) {
		super(scope, agent, param, l);
	}

	LabelEditor(final IScope scope, final Composite parent, final String title, final Object value,
			final EditorListener<String> whenModified) {
		// Convenience method
		super(scope, new InputParameter(title, value), whenModified);
		this.createComposite(parent);

	}

	@Override
	public void modifyText(final ModifyEvent me) {
		if (internalModification) { return; }
		modifyValue(textBox.getText());
	}

	@Override
	protected Control createCustomParameterControl(final Composite comp) {
		textBox = new Text(comp, SWT.BORDER);
		textBox.addModifyListener(this);
		return textBox;
	}

	@Override
	protected void displayParameterValue() {
		String s = currentValue;
		if (s == null) {
			s = "";
		}
		textBox.setText(s);
	}

	@Override
	public Control getEditorControl() {
		return textBox;
	}

	@Override
	protected int[] getToolItems() {
		return new int[] { REVERT };
	}

}
