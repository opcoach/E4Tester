package com.opcoach.e4tester.test.components.test;

import static org.junit.Assert.assertNotNull;

import org.eclipse.e4.ui.model.application.descriptor.basic.MPartDescriptor;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.junit.Before;
import org.junit.Test;

import com.opcoach.e4tester.core.E4TestCase;
import com.opcoach.e4tester.test.components.Part1;

public class Part1Test extends E4TestCase {

	private MPart part;

	@Before
	public void setup() {
		part = createTestPart(Part1.ID);
	}

	@Test
	public void testPart1IsCreated() {
		assertNotNull("The part1 must be created", part);
	}
	
	@Test
	public void testPartDescriptorExists() {
		// The fragment in components contains this descriptor. Must check it is in applciation. 
		MPartDescriptor elt = null;

		for (MPartDescriptor mp : getApplication().getDescriptors())
			if (Part1.ID.equals(mp.getElementId())) {
				elt = mp;
				break;
			}

		assertNotNull("The part1 descriptor must be included in the application", elt);
	}
	
	@Test
	public void testLabelValue() {
		assertLabelContains(part, "label", Part1.LABEL_VALUE, "The label in Part1 must have a right value");
	}



	
}
