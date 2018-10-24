package com.opcoach.e4tester.core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.core.WrongFieldTypeException;
import com.opcoach.e4tester.core.WrongFieldnameException;
import com.opcoach.e4tester.test.components.Part1;

public class WidgetAccessTest extends E4TestCase {

	private MPart part;

	@Before
	public void setup() {
		part = createTestPart(Part1.ID);
	}

	@Test
	public void testPartDescriptorExists() {
		MPartDescriptor elt = null;

		for (MPartDescriptor mp : getApplication().getDescriptors())
			if (Part1.ID.equals(mp.getElementId())) {
				elt = mp;
				break;
			}

		assertNotNull("The part1 descriptor must be included in the application", elt);
	}

	@Test
	public void testCreateContent() {

		assertNotNull("The sample part1 must be created", part);
	}

	@Test
	public void testLabelValue() {
		assertLabelContains(part, "label", Part1.LABEL_VALUE, "The label in Part1 must have a right value");
	}

	@Test
	public void testFindTreeviewer() {
		TreeViewer tv = getTreeViewer(part, "tv");
		assertNotNull("The treeviewer tv must be found", tv);

		// tv.expandToLevel(2);

		// Check tree contains :
		/*
		 * String1 String11 String12 String13 String14 String15 String2 String21
		 * String22 String23 String24 String25
		 */

	}

	@Test
	public void testButtonSelected() {
		assertTrue("The selected checkbox button must be selected", isButtonChecked(part, "checkboxSelected"));
	}

	@Test
	public void testButtonSelectedHasRightText() {
		assertEquals("The selected checkbox button must be selected", Part1.CHECKBOX_SELECTED_TEXT,
				getTextWidgetValue(part, "checkboxSelected"));
	}

	@Test
	public void testButtonNotSelected() {
		assertFalse("The notselected checkbox button must be selected", isButtonChecked(part, "checkboxNotSelected"));
	}

	@Test
	public void testButtonNotSelectedHasRightText() {
		assertEquals("The notselected checkbox button must be selected", Part1.CHECKBOX_NOTSELECTED_TEXT,
				getTextWidgetValue(part, "checkboxNotSelected"));
	}

	@Test(expected = WrongFieldnameException.class)
	public void testFieldnameForWidgetIsWrong() {
		getInstanceValue(part, "adummyfield");
	}

	@Test(expected = WrongFieldTypeException.class)
	public void testFieldTypeForWidgetIsWrong() {
		getTreeViewer(part, "label");  // Label is not a TreeViewer.
	}

}
