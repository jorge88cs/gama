/*
 * generated by Xtext
 */
package gama.core.lang.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import gama.core.lang.GamlRuntimeModule;
import gama.core.lang.GamlStandaloneSetup;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class GamlIdeSetup extends GamlStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new GamlRuntimeModule(), new GamlIdeModule()));
	}
	
}
