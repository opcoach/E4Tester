package com.opcoach.e4tester.core;

import org.eclipse.pde.internal.junit.runtime.UITestApplication;

public class E4TesterUITestApplication extends UITestApplication {

	

	@Override
	protected Object runApp(IApplication app, IApplicationContext context) throws Exception {
		// Get the testable object from the service
		Object testableObject = PDEJUnitRuntimePlugin.getDefault().getTestableObject();
		// If the service doesn't return a testable object ask PlatformUI directly
		// Unlike in NonUIThreadTestApplication if the platform dependency is not available we will fail here
		fTestHarness = new PlatformUITestHarness(testableObject, true);

		// continue application launch
		E4TesterApplication e4tester = new E4TesterApplication();
		return e4tester.start(context);// super.runApp(app, context, args);
	}
	
}
