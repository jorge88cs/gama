/*********************************************************************************************
 *
 * 'IParameterEditor.java, in plugin ummisco.gama.ui.shared, is part of the source code of the GAMA modeling and
 * simulation platform. (v. 1.8.1)
 *
 * (c) 2007-2020 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 * 
 *
 **********************************************************************************************/
package ummisco.gama.ui.interfaces;

import msi.gama.common.interfaces.IScoped;
import msi.gama.kernel.experiment.IParameter;
import msi.gaml.types.IType;

/**
 * The class IParameterEditor.
 * 
 * @author drogoul
 * @since 18 d�c. 2011
 * 
 */
@SuppressWarnings ({ "rawtypes" })
public interface IParameterEditor<T> extends IScoped {

	public abstract IType getExpectedType();

	public abstract boolean isValueModified();

	public abstract void revertToDefaultValue();

	public abstract IParameter getParam();

	public abstract void updateValue(boolean force);

	public abstract void forceUpdateValueAsynchronously();

	public abstract void setActive(Boolean value);

	public T getCurrentValue();

	/**
	 * Items to add to the editor
	 */

	static final int PLUS = 0;
	static final int MINUS = 1;
	static final int EDIT = 2;
	static final int INSPECT = 3;
	static final int BROWSE = 4;
	static final int CHANGE = 5;
	static final int REVERT = 6;
	static final int DEFINE = 7;

	/**
	 * @param b
	 */
	public abstract void isSubParameter(boolean b);

}