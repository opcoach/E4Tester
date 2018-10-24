package com.opcoach.e4tester.test.components.test;



import static org.junit.Assert.assertEquals;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;

public class EventTopicTest extends E4TestCase {

	private MPart part1;

	@Before public void setup()
	{
		part1 = createTestPart(Part1.ID);
	}
	
	
	@Test
	public void testGetEventBrokerSend()  {
		
		String sentString = "eventString";
		getEventBroker().send("topicTest", sentString);
		
		assertEquals("Label must contain 'eventString'",sentString,  getTextWidgetValue(part1, "label"));

	}
	
	

}
