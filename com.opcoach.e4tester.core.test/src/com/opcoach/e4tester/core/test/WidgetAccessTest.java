package com.opcoach.e4tester.core.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.viewers.TreeViewer;
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
	// JUnit 5 : @DisplayName("Label widget has a good value")
	public void testLabelValue(){

		assertEquals("The label in Part1 must have a right value", Part1.LABEL_VALUE, getTextWidgetValue(part, "label"));
	}
	
	
	@Test
	public void testDeployTree(){
		TreeViewer tv = getTreeViewer(part, "tv");
		tv.expandToLevel(2);
		
		// Check tree contains : 
		/*
		  String1
		  	String11
		  	String12
		  	String13
		  	String14
		  	String15
		   String2
		  	String21
		  	String22
		  	String23
		  	String24
		  	String25
		  	*/
		  
		assertNotNull("The treeviewer tv must be found", tv);
	}
	
	
	
	

}
