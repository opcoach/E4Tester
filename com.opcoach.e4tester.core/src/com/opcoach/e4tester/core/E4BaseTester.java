package com.opcoach.e4tester.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.tools.context.spy.ContextSpyPart;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.SelectionServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.opcoach.e4tester.core.stubs.E4TesterLogger;

/**
 * This basic class can be used as parent class for any E4 POJO test It
 * initializes osgi and MApplication context so as to be able to test the POJO
 * It also provides a basic application (with nothing inside), in case it is
 * injected in some POJOs All methods provided in this class are protected and
 * can be overriden
 * 
 * @author olivier
 *
 */
public abstract class E4BaseTester {

	protected static IEclipseContext ctx;
	private static IEclipseContext osgiCtx;

	/** This global setup initializes basic contexts for tests */
	@BeforeClass // See issue #3 (https://github.com/opcoach/E4Tester/issues/3), replace with BeforeAll later
	public static void globalSetup() throws Exception {

		System.out.println("Enter in globalSetup ");

		Bundle e4Bundle = Platform.getBundle("org.eclipse.e4.ui.workbench");

		BundleContext e4BundleContext = e4Bundle.getBundleContext();
		osgiCtx = EclipseContextFactory.getServiceContext(e4BundleContext);
		osgiCtx.set("myKeyInOsgi1", "value");
		
		ModelServiceImpl ms = new ModelServiceImpl(osgiCtx);
		ContextInjectionFactory.inject(ms, osgiCtx);
		osgiCtx.set(EModelService.class, ms);
		
		MApplication appli = ms.createModelElement(MApplication.class);


		ctx = EclipseContextFactory.create("TestContext");
		ctx.setParent(osgiCtx);
		ctx.set(IEclipseContext.class, ctx);

		ctx.set(ESelectionService.class, ContextInjectionFactory.make(SelectionServiceImpl.class, ctx));

		E4TesterLogger log = ContextInjectionFactory.make(E4TesterLogger.class, ctx);
		ctx.set(Logger.class, log);

		// _____> IL FAUDRAIT CREER UNE APPLI PARTICULIERE POUR AVOIR LES SPIES ??
		appli.setContext(ctx);
		osgiCtx.set(MApplication.class, appli); 
		


	}

	protected static Shell getShell() {
		Shell s = ctx.get(Shell.class);
		if (s == null) {
			s = new Shell(Display.getCurrent());
			s.setText("Test window");
			ctx.set(Composite.class, s);
		}
		return s;
	}

	protected MApplication getApplication() {
		return getContext().get(MApplication.class);
	}

	protected IEclipseContext getOsgiContext() {
		return osgiCtx;
	}

	protected IEclipseContext getContext() {
		return ctx;
	}

	public void wait1second() {
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void openContextSpy() {
		System.out.println("Opening SpyContext");
		Display d = Display.getCurrent();
		Shell s = new Shell(d);
		s.setLayout(new GridLayout(1, true));
		s.setText("Context Spy");
		IEclipseContext spyCtx = EclipseContextFactory.create("SpyContext");
		spyCtx.setParent(ctx);
		spyCtx.set(Composite.class, s);
		ContextSpyPart cs = ContextInjectionFactory.make(ContextSpyPart.class, spyCtx);

		Job j = new Job("UI") {
			protected IStatus run(IProgressMonitor monitor) {
				s.pack();
				s.open();

				while (!s.isDisposed()) {
					if (!d.readAndDispatch())
						d.sleep();
				}

				System.out.println("End Opening SpyContext");
				return Status.OK_STATUS;

			}
		};
		j.schedule();
	}

}
