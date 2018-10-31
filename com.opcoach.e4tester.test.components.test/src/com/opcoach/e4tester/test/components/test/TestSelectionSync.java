package com.opcoach.e4tester.test.components.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class TestSelectionSync extends E4TestCase {

	private MPart part1, part2;

	@BeforeEach public void setup()
	{
			part1 = createTestPart(Part1.ID);
			part2 = createTestPart(Part2.ID);
	}

	@Test
	public void testSelectObjectInTreeViewer()  {
				
		String expected = "String1";
		selectObjectInTreeViewer(part1, "tv", expected);
		
		wait1second();
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as selected tree node");
		
		// Or can be written like this : 
		assertLabelContains(part2, "label", expected, "Text in label must be as selected tree node");
	}

}
