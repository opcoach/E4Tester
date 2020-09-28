package com.opcoach.e4tester.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class SelectionTest extends E4TestCase {

	private MPart part1, part2;

	@BeforeEach public void setup()
	{
		part1 = createTestPart(Part1.ID);
		part2 = createTestPart(Part2.ID);
		
	}
	
	@Test
	public void testgetSelectionService()  {
		//ESelectionService sel = getSelectionService();
		String expected = "ASampleString";
		setSelectionService(expected);
		//sel.setSelection(expected);
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as expected after selection");
	}
	
	@Test
	public void testSelectObjectInTreeViewer()  {
		
		
		////// MUST BE UPDATED... IT WORKS IF PARTS ARE CREATED IN A GIVEN ORDER (See setup )...
		///   IF setup is initialized with part1 then part2, it will fail.... 
		
		String expected = "String11";
		selectObjectInTreeViewer(part1, "tv", expected);
		//wait1second();
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as selected tree node");
	}
	@Test
	public void testgetTreeviewerAndSelectObjectInTreeViewer()  {
		
		String expected = "String12";
		TreeViewer tv = getTreeViewer(part1, "tv");
		Display.getDefault().syncExec(()-> {
			tv.setSelection(new TreeSelection(new TreePath( new Object[] {expected} )));
		});
		assertEquals(expected, getTextWidgetValue(part2, "label"), "Text in label must be as selected tree node");
	}
}
