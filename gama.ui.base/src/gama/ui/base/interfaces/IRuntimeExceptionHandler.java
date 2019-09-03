/*******************************************************************************************************
 *
 * msi.gama.common.interfaces.IRuntimeExceptionHandler.java, in plugin msi.gama.core,
 * is part of the source code of the GAMA modeling and simulation platform (v. 1.8)
 * 
 * (c) 2007-2018 UMI 209 UMMISCO IRD/SU & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and contacts.
 * 
 ********************************************************************************************************/
package gama.ui.base.interfaces;

import java.util.List;

import gama.runtime.exceptions.GamaRuntimeException;

public interface IRuntimeExceptionHandler {

	void start();

	void stop();

	void clearErrors();

	void offer(final GamaRuntimeException ex);

	void remove(GamaRuntimeException obj);

	List<GamaRuntimeException> getCleanExceptions();

	boolean isRunning();

}
