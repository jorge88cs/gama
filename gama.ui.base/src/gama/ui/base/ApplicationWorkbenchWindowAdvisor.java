/*********************************************************************************************
 *
 * 'ApplicationWorkbenchWindowAdvisor.java, in plugin gama.core.application, is part of the source code of the GAMA
 * modeling and simulation platform. (c) 2007-2016 UMI 209 UMMISCO IRD/UPMC & Partners
 *
 * Visit https://github.com/gama-platform/gama for license information and developers contact.
 *
 *
 **********************************************************************************************/
package gama.ui.base;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.internal.ide.application.IDEWorkbenchWindowAdvisor;
import org.osgi.framework.Bundle;

import gama.ui.base.bindings.GamaKeyBindings;
import gama.ui.base.commands.TestsRunner;
import gama.ui.base.utils.CleanupHelper;
import gama.ui.base.utils.PerspectiveHelper;
import gama.common.preferences.GamaPreferences;
import gama.runtime.GAMA;

public class ApplicationWorkbenchWindowAdvisor extends IDEWorkbenchWindowAdvisor {

	@Override
	public ActionBarAdvisor createActionBarAdvisor(final IActionBarConfigurer configurer) {
		return new GamaActionBarAdvisor(configurer);
	}

	public ApplicationWorkbenchWindowAdvisor(final ApplicationWorkbenchAdvisor adv,
			final IWorkbenchWindowConfigurer configurer) {
		super(adv, configurer);

		// Hack and workaround for the inability to find launcher icons...

		final Bundle bundle = Platform.getBundle("gama.core.application");

		final ImageDescriptor myImage =
				ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("branding_icons/icon256.png"), null));
		configurer.getWindow().getShell().setImage(myImage.createImage());
	}

	@Override
	public void preWindowOpen() {
		super.preWindowOpen();
		final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

		configurer.getWindow().addPerspectiveListener(new IPerspectiveListener() {

			@Override
			public void perspectiveChanged(final IWorkbenchPage page, final IPerspectiveDescriptor perspective,
					final String changeId) {}

			@Override
			public void perspectiveActivated(final IWorkbenchPage page, final IPerspectiveDescriptor perspective) {
				if (PerspectiveHelper.getInstance().isSimulationPerspective()) {
					// DEBUG.OUT("Running the perspective listener to automatically launch modeling");
					final IPerspectiveDescriptor desc = page.getPerspective();
					page.closePerspective(desc, false, false);
					PerspectiveHelper.getInstance().openModelingPerspective(true, false);
				}
				configurer.getWindow().removePerspectiveListener(this);

			}
		});
		configurer.getWindow().addPageListener(new IPageListener() {

			@Override
			public void pageActivated(final IWorkbenchPage page) {
				configurer.getWindow().removePageListener(this);
				PerspectiveHelper.getInstance().openModelingPerspective(true, false);
			}

			@Override
			public void pageClosed(final IWorkbenchPage page) {}

			@Override
			public void pageOpened(final IWorkbenchPage page) {}
		});
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShowProgressIndicator(true);
		configurer.setShowPerspectiveBar(false);
		configurer.setTitle(GAMA.VERSION);

	}

	@Override
	public void postWindowRestore() throws WorkbenchException {}

	@Override
	public void postWindowCreate() {
		CleanupHelper.run();
		GamaKeyBindings.install();
		final IWorkbenchWindow window = getWindowConfigurer().getWindow();
		window.getShell().setMaximized(GamaPreferences.Interface.CORE_SHOW_MAXIMIZED.getValue());
		if (GamaPreferences.Runtime.START_TESTS.getValue()) {
			TestsRunner.start();
		}
	}

	@Override
	public void postWindowOpen() {
		PerspectiveHelper.getInstance().cleanPerspectives();
		GAMA.getGui().openWelcomePage(true);
	}

}
