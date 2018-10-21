package com.opcoach.e4tester.core;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.E4Application;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Use this application to launch the tests extending E4TestCase 
 * @author olivier
 */
public class E4TesterApplication extends E4Application {

	private static E4Workbench e4workbench = null;
	private static E4Application e4Appli = null;
	private static boolean appliIsInitialized = false;

	public static E4Application getE4Appli() {
		return e4Appli;
	}

	public static E4Workbench getE4workbench() {
		// Get the part service in E4 Workbench and ensure that everything is
		// initialized
		// IE : there is an active Child on current applciation
		/*
		 * synchronized (e4workbench) { IEclipseContext ctx =
		 * e4workbench.getApplication().getContext().getActiveChild(); while (ctx ==
		 * null) { System.out.println("Waiting for e4workbench"); try {
		 * e4workbench.wait(); } catch (InterruptedException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } }
		 * 
		 * }
		 */

		return e4workbench;
	}

	/**
	 * Take hand on the default E4 Application and launch the tests in another
	 * thread.
	 */
	@Override
	public Object start(IApplicationContext applicationContext) throws Exception {
		// set the display name before the Display is
		// created to ensure the app name is used in any
		// platform menus, etc. See
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=329456#c14
		IProduct product = Platform.getProduct();
		if (product != null && product.getName() != null) {
			Display.setAppName(product.getName());
		}
		Display display = getApplicationDisplay();

		Location instanceLocation = null; // Do not manage instance location for tests...
		try {
			e4Appli = this;
			e4workbench = createE4Workbench(applicationContext, display);

			Shell shell = display.getActiveShell();
			if (shell == null) {
				shell = new Shell();
				// place it off so it's not visible
				shell.setLocation(0, 0);
			}

			e4workbench.getApplication().setOnTop(true);

			// Create and run the UI (if any)
			e4workbench.createAndRunUI(e4workbench.getApplication());

			saveModel();
			e4workbench.close();

			if (e4workbench.isRestart()) {
				return EXIT_RESTART;
			}

			return EXIT_OK;
		} finally {
			if (display != null)
				display.dispose();
			if (instanceLocation != null)
				instanceLocation.release();
		}
	}

}
