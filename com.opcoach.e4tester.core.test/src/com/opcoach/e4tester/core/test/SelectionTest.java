package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class SelectionTest extends E4TestCase {

	private MPart part1, part2;

	@Before public void setup()
	{
		part2 = createTestPart(Part2.ID);
		part1 = createTestPart(Part1.ID);
	}
	
	@Test
	public void testgetSelectionService()  {
		ESelectionService sel = getSelectionService();
		String expected = "ASampleString";
		sel.setSelection(expected);
		assertEquals("Text in label must be as expected after selection",expected, getTextWidgetValue(part2, "label"));
	}
	
	@Test
	public void testSelectObjectInTreeViewer()  {
		
		
		////// MUST BE UPDATED... IT WORKS IF PARTS ARE CREATED IN A GIVEN ORDER (See setup )...
		///   IF setup is initialed with part1 then part2, it will fail.... 
		
		String expected = "String11";
		selectObjectInTreeViewer(part1, "tv", expected);
		//tv.setSelection(new TreeSelection(new TreePath( new Object[] {expected} )));
		wait1second();
		assertEquals("Text in label must be as selected tree node",expected, getTextWidgetValue(part2, "label"));
	}

}
