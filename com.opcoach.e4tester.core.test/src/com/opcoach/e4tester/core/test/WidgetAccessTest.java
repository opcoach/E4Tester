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
	public void testCreatePart() {

		assertNotNull("The sample part1 must be created", part);
	}


	@Test
	public void testGetTreeViewer() {
		TreeViewer tv = getTreeViewer(part, "tv");
		assertNotNull("The treeviewer tv must be found", tv);
	}

	@Test
	public void testIsButtonCheckedReturnsTrue() {
		assertTrue("The selected checkbox button must be selected", isButtonChecked(part, "checkboxSelected"));
	}

	@Test
	public void testGetTextWidgetValueOnButton() {
		assertEquals("The selected checkbox button must be selected", Part1.CHECKBOX_SELECTED_TEXT,
				getTextWidgetValue(part, "checkboxSelected"));
	}

	@Test
	public void testIsButtonCheckedReturnsFalse() {
		assertFalse("The notselected checkbox button must be selected", isButtonChecked(part, "checkboxNotSelected"));
	}


	@Test(expected = WrongFieldnameException.class)
	public void testGetInstanceValueBadFielName() {
		getInstanceValue(part, "adummyfield");
	}

	@Test(expected = WrongFieldTypeException.class)
	public void testGetTreeViewerBadFieldType() {
		getTreeViewer(part, "label");  // Label is not a TreeViewer.
	}

}
