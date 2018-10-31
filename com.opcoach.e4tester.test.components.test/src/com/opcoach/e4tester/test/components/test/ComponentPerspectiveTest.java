package com.opcoach.e4tester.test.components.test;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.ComponentsConstants;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class ComponentPerspectiveTest extends E4TestCase {


	@Test
	public void testPerspectiveContent() {
		MPerspective p = showPerspective(ComponentsConstants.PERSPECTIVE_ID);
		
		assertNotNull(p, "The perspective must exists");
		// The component perspective must always contain P1 and P2. 
		MUIElement p1 = getModelService().find(Part1.ID, p);
		MUIElement p2 = getModelService().find(Part2.ID, p);
		
		assertNotNull(p1,"The part P1 must be in the perspective");
		assertNotNull(p2,"The part P2 must be in the perspective");

		
		

	}

}
