package com.opcoach.e4tester.core;

import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.pde.internal.junit.runtime.UITestApplication;

public class E4TesterUITestApplication extends UITestApplication {

	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("E4TesterUITestApplication.start() called");
		
		return super.start(context);
	}

	
	
}
