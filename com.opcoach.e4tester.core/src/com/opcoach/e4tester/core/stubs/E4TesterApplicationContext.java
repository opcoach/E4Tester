package com.opcoach.e4tester.core.stubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class E4TesterApplicationContext implements IApplicationContext {

	
	Map<String, Object> applicationMap =  null; 

	
	@Override
	public Map<String, Object> getArguments() {
		if (applicationMap == null)
		{
			String[] applicationArgs = Platform.getApplicationArgs();
			String[] newArgs = new String[applicationArgs.length + 2];
			
			int i;
			for (i = 0; i < applicationArgs.length; i++)
			{
				newArgs[i] = applicationArgs[i];
			}
			newArgs[i++] = "-" + IWorkbench.XMI_URI_ARG;
			newArgs[i++] =  FrameworkUtil.getBundle(getClass()).getSymbolicName() + "/E4TesterApplication.e4xmi"; 
			
			applicationMap = parseArguments(newArgs);
		}
		return applicationMap;
	}
	

	private Map<String, Object> parseArguments(String[] arguments) {
		Map<String, Object> applicationMap = new HashMap<String, Object>();
		Map<String, String> args = new HashMap<String, String>(arguments.length);
		for (int i = 0; i < arguments.length; i++) {
			if (i == arguments.length - 1 || arguments[i + 1].startsWith("-"))
				args.put(arguments[i], null);
			else
				args.put(arguments[i], arguments[++i]);
		}

	
		applicationMap.put(IApplicationContext.APPLICATION_ARGS, flattenMap(args));
		return applicationMap;
	}

	private String[] flattenMap(Map<String, String> map) {
		ArrayList<String> list = new ArrayList<String>(map.size());
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			String value = map.get(key);
			list.add(key);
			if (value != null)
				list.add(value);
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public void applicationRunning() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBrandingApplication() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBrandingName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBrandingDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBrandingId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBrandingProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getBrandingBundle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResult(Object result, IApplication application) {
		// TODO Auto-generated method stub

	}

}
