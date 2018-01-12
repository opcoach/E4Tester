package com.opcoach.e4tester.core;


import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.internal.workbench.ModelServiceImpl;
import org.eclipse.e4.ui.internal.workbench.SelectionServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeAll;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.opcoach.e4tester.core.stubs.E4TesterLogger;


/** This basic class can be used as parent class for any E4 POJO test 
 * It initializes osgi and MApplication context so as to be able to test the POJO 
 * It also provides a basic application (with nothing inside), in case it is injected in some POJOs
 *  All methods provided in this class are protected and can be overriden  
 * @author olivier
 *
 */
public abstract class E4BaseTester {

	protected static IEclipseContext ctx;
	private static IEclipseContext osgiCtx;

	/** This global setup initializes basic contexts for tests */
	@BeforeAll
	public static void globalSetup() throws Exception {

		System.out.println("Enter in globalSetup ");

		Bundle e4Bundle = Platform.getBundle("org.eclipse.e4.ui.workbench");
 
		BundleContext e4BundleContext = e4Bundle.getBundleContext();
		 osgiCtx = EclipseContextFactory.getServiceContext(e4BundleContext);
		osgiCtx.set("myKeyInOsgi1", "value");

		ctx = EclipseContextFactory.create("TestContext");
		ctx.setParent(osgiCtx);
		ctx.set(IEclipseContext.class, ctx);
		
		ctx.set(ESelectionService.class, ContextInjectionFactory.make(SelectionServiceImpl.class, ctx));
		ModelServiceImpl ms = new ModelServiceImpl(ctx);
		ContextInjectionFactory.inject(ms, ctx);
		ctx.set(EModelService.class, ms);
		E4TesterLogger log = ContextInjectionFactory.make(E4TesterLogger.class, ctx);
		ctx.set(Logger.class, log);
		
		
		//  _____>  IL FAUDRAIT CREER UNE APPLI PARTICULIERE POUR AVOIR LES SPIES ??
		MApplication appli = ms.createModelElement(MApplication.class);
		appli.setContext(ctx);
		osgiCtx.set(MApplication.class, appli);
		
		
	}
	
	protected static Shell getShell() {
		Shell s = ctx.get(Shell.class);
		if (s == null)
		{
		    s = new Shell(Display.getCurrent());
			s.setText("Test window");
			ctx.set(Composite.class, s);

		}
		return s;
	}
	
	protected MApplication getApplication() {
		return getContext().get(MApplication.class);
	}
	
	
	
	protected IEclipseContext getOsgiContext()
	{
		return osgiCtx;
	}
	
	protected IEclipseContext getContext()
	{
		return ctx;
	}
	
	public void wait1second()  {
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
