package com.opcoach.e4tester.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Combo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.core.WrongFieldTypeException;
import com.opcoach.e4tester.core.WrongFieldnameException;
import com.opcoach.e4tester.core.stubs.E4TesterLogger;
import com.opcoach.e4tester.test.components.Part1;

public class WidgetAccessTest extends E4TestCase {

	private MPart part;

	private E4TesterLogger theLogger;
	
	
	
	@BeforeEach
	public void setup() {
		theLogger = (E4TesterLogger) getApplication().getContext().get(E4TesterLogger.E4TEST_LOGGER);
		assertNotNull(theLogger);
		theLogger.info("create test part id:" + Part1.ID);
		part = createTestPart(Part1.ID);
	}

	@Test
	public void testCreatePart() {

		assertNotNull(part, "The sample part1 must be created");
	}

	@Test
	public void testGetTreeViewer() {
		TreeViewer tv = getTreeViewer(part, "tv");
		assertNotNull(tv, "The treeviewer must be found");
	}

	@Test
	public void testCombo() {
		Combo c = getCombo(part, "combobox");
		assertNotNull(c, "The combo  must be found");
	}

	@Test
	public void testIsButtonCheckedReturnsTrue() {
		assertTrue(isButtonChecked(part, "checkboxSelected"), "The selected checkbox button must be selected");
	}

	@Test
	public void testGetTextWidgetValueOnButton() {
		assertEquals(Part1.CHECKBOX_SELECTED_TEXT, getTextWidgetValue(part, "checkboxSelected"),
				"The selected checkbox button must be selected");
	}

	@Test
	public void testIsButtonCheckedReturnsFalse() {
		assertFalse(isButtonChecked(part, "checkboxNotSelected"), "The notselected checkbox button must be selected");
	}

	@Test
	public void testGetInstanceValueBadFielName() {
		assertThrows(WrongFieldnameException.class,

				() -> {
					getInstanceValue(part, "adummyfield");
				}, "adummyfield is not a field");

	}

	@Test
	public void testGetTreeViewerBadFieldType() {
		assertThrows(WrongFieldTypeException.class, () -> {
			getTreeViewer(part, "label");
		});

	}

	@Test
	public void testComboBadFieldType() {
		// Label is not a Combo.
		assertThrows(WrongFieldTypeException.class, () -> {
			getCombo(part, "label");
		});
	}

}
