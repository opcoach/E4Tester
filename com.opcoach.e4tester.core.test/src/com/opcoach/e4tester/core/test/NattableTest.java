package com.opcoach.e4tester.core.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.selection.command.SelectCellCommand;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.core.stubs.E4TesterLogger;
import com.opcoach.e4tester.test.nattable.components.NattablePart1;


@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class NattableTest extends E4TestCase{

	private MPart natPart1;

	private E4TesterLogger theLogger;
	
	@BeforeEach
	public void setup() {
		theLogger = (E4TesterLogger) getApplication().getContext().get(E4TesterLogger.E4TEST_LOGGER);
		theLogger.info("create natTable  part id:" + NattablePart1.ID);
		natPart1 = createTestPart(NattablePart1.ID);
		assertNotNull(natPart1);
	}
	@Order(1)
	@Disabled
	@Test
	public void getNattableWidget() {
		NatTable ntable = getNatTable(natPart1, "natTable");
		assertNotNull(ntable);
	}
	
	@Order(2)
	@Disabled
	@Test
	public void getNattableOutputAera(){
		String text = getTextWidgetValue(natPart1, "outputArea");
		assertNotNull(text);
		assertEquals("Init",text);
		setTextWidgetValue(natPart1.getObject(), "outputArea","");
		assertTrue(getTextWidgetValue(natPart1, "outputArea").isEmpty());
	}
	
	@Order(3)
	@Test
	public void getNatTableCellValue() throws InterruptedException {
		NatTable ntable = getNatTable(natPart1, "natTable");
		assertNotNull(ntable);
		setTextWidgetValue(natPart1.getObject(), "outputArea","");
		Display.getDefault().syncExec(()->{
			ntable.doCommand(new SelectCellCommand(ntable,1,10,false,false));	
		});
		waitseconds(1);
		//
		String text = getTextWidgetValue(natPart1, "outputArea");
		assertNotEquals("",text);

	}
}
