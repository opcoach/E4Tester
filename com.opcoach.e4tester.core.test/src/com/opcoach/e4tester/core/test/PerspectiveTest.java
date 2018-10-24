package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.ComponentsConstants;
import com.opcoach.e4tester.test.components.Part1;

public class PerspectiveTest extends E4TestCase {
	
	
	@Test
	public void testShowPerspective()  {
		
		MPerspective p = showPerspective(ComponentsConstants.PERSPECTIVE_ID);
		
		assertNotNull("The perspective must exists", p);

	}
	
	

}
