package com.opcoach.e4tester.test.components.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;

public class EventTopicTest extends E4TestCase {

	private MPart part1;

	@BeforeEach public void setup()
	{
		part1 = createTestPart(Part1.ID);
	}
	
	
	@Test
	public void testGetEventBrokerSend()  {
		
		String sentString = "eventString";
		getEventBroker().send("topicTest", sentString);
		
		assertEquals(sentString,  getTextWidgetValue(part1, "label"), "Label must contain 'eventString'");

	}
	
	

}
