package com.opcoach.e4tester.core.stubs;

import org.eclipse.e4.core.services.log.Logger;

public class E4TesterLogger extends Logger {

	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void error(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void warn(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void info(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void trace(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void debug(Throwable t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void debug(Throwable t, String message) {
		// TODO Auto-generated method stub

	}

}
