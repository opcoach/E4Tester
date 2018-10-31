package com.opcoach.e4tester.core;

/** This exception is thrown when a field is found in a pojo but not with the expected type
 * This is a runtime exception to avoid any try catch in the test code
 * @author olivier
 *
 */
public class WrongFieldTypeException extends RuntimeException {

	private static final long serialVersionUID = -7063007322553936125L;
	
	private String fn;
	private Object pojo; 
	private Class<?> expected;
	private Class<?> obtained;
	
	public WrongFieldTypeException(String fieldname,  Object pojoInstance, Class<?> expectedClass, Class<?> realClass) {
		fn = fieldname; 
		pojo = pojoInstance;
		expected = expectedClass;
		obtained = realClass;
	}
	
	@Override
	public String getMessage() {
		return "The field with name '" + fn + "' is expected to be of type " + expected.getCanonicalName() + " but is actually " + obtained.getCanonicalName();
	}


}
