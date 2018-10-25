package com.opcoach.e4tester.test.components.test;

import static org.junit.Assert.*;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;
import com.opcoach.e4tester.test.components.Part2;

public class TestSelectionSync extends E4TestCase {

	private MPart part1, part2;

	@Before public void setup()
	{
			part1 = createTestPart(Part1.ID);
			part2 = createTestPart(Part2.ID);
	}

	@Test
	public void testSelectObjectInTreeViewer()  {
				
		String expected = "String1";
		selectObjectInTreeViewer(part1, "tv", expected);
		
		wait1second();
		assertEquals("Text in label must be as selected tree node",expected, getTextWidgetValue(part2, "label"));
		
		// Or can be written like this : 
		assertLabelContains(part2, "label", expected, "Text in label must be as selected tree node");
	}

}
