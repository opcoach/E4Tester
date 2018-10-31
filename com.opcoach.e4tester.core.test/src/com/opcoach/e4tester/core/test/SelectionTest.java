package com.opcoach.e4tester.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class SelectionTest extends E4TestCase {

	private MPart part1, part2;

	@BeforeEach public void setup()
	{
		part2 = createTestPart(Part2.ID);
		part1 = createTestPart(Part1.ID);
	}
	
	@Test
	public void testgetSelectionService()  {
		ESelectionService sel = getSelectionService();
		String expected = "ASampleString";
		sel.setSelection(expected);
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as expected after selection");
	}
	
	@Test
	public void testSelectObjectInTreeViewer()  {
		
		
		////// MUST BE UPDATED... IT WORKS IF PARTS ARE CREATED IN A GIVEN ORDER (See setup )...
		///   IF setup is initialed with part1 then part2, it will fail.... 
		
		String expected = "String11";
		selectObjectInTreeViewer(part1, "tv", expected);
		//tv.setSelection(new TreeSelection(new TreePath( new Object[] {expected} )));
		wait1second();
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as selected tree node");
	}

}
