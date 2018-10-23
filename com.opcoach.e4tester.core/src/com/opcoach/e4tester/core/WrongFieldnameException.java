package com.opcoach.e4tester.core;

/** This exception is thrown when a field is not found in a pojo. 
 * This is a runtime exception to avoid any try catch in the test code
 * @author olivier
 *
 */
public class WrongFieldnameException extends RuntimeException {

	private static final long serialVersionUID = 1282388926252496082L;

	private String fn;
	private Object pojo; 
	
	public WrongFieldnameException(String fieldname, Object pojoInstance) {
		fn = fieldname; 
		pojo = pojoInstance;
	}
	
	@Override
	public String getMessage() {
		return "The field with name '" + fn + "' can not be found in the pojo class : " + pojo.getClass().getCanonicalName();
	}


}
