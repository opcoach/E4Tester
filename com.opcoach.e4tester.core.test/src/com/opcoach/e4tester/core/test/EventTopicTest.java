package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.core.test.components.Part1;

public class EventTopicTest extends E4TestCase {

	private MPart part;

	@Before public void setup()
	{
		part = createTestPart(Part1.ID);
	}
	
	
	@Test
	public void testEvent()  {
		
		String sentString = "eventString";
		getEventBroker().send("topicTest", sentString);
		
		assertEquals("Label must contain 'eventString'",sentString,  getTextWidgetValue(part, "label"));

	}

}
