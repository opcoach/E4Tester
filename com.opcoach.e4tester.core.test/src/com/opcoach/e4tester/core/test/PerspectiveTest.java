package com.opcoach.e4tester.core.test;



import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.ComponentsConstants;

public class PerspectiveTest extends E4TestCase {
	
	
	@Test
	public void testShowPerspective()  {
		
		MPerspective p = showPerspective(ComponentsConstants.PERSPECTIVE_ID);
		
		assertNotNull(p,"The perspective must exists");

	}
	
	

}
