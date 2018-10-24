package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part2;

public class SelectionTest extends E4TestCase {

	private MPart part;

	@Before public void setup()
	{
		part = createTestPart(Part2.ID);
	}
	
	
	@Test
	public void testSelection()  {
		ESelectionService sel = getSelectionService();
		String expected = "ASampleString";
		sel.setSelection(expected);

		assertEquals("Text in label must be as expected after selection",expected, getTextWidgetValue(part, "label"));
		
	}

}
