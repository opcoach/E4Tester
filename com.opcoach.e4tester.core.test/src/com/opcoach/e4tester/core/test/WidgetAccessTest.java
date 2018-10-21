package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.core.test.components.Part1;

public class WidgetAccessTest extends E4TestCase {

	private MPart part;

	@Before public void setup()
	{
		part = createTestPart(Part1.ID);
	}
	
	@Test
	public void testPartDescriptorExists()
	{
		EModelService ms = getModelService();
		MPartDescriptor elt = null;
		
		for (MPartDescriptor mp : getApplication().getDescriptors())
			if (Part1.ID.equals(mp.getElementId()))
			{
				elt = mp;
				break;
			}
		
		assertNotNull("The part1 descriptor must be included in the application", elt);
	}
	
	@Test
	// JUnit 5 : @DisplayName("Label widget is found")
	public void testCreateContent(){

		// JUnit 5 : assertNotNull(part, "The sample part1 must be created" );
		assertNotNull("The sample part1 must be created", part);
	}
	
	
	@Test
	// JUnit 5 : @DisplayName("Label widget is found")
	public void testEvent()  {
		IEventBroker eb = getEventBroker();
		eb.send("topicTest", "eventString");
		// JUnit 5 : assertNotNull(part, "The sample part1 must be created" );
		//waitseconds(5);
		String lab = getTextWidgetValue(part, "label");

		// JUnit 5 : assertEquals(objectLabel, "Perceuse Electrique", "La location par
		// défaut doit afficher perceuse electrique");
		assertEquals("Label must contain 'eventString'","eventString",  lab);

	}

}
