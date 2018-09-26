 
package com.opcoach.e4tester.core.test.components;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class Part1 {
	
	public static final String ID = "com.opcoach.e4tester.core.test.part1";
	
	private Label label;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setText("xxxxxxxxxxxxxxxx");
	}
	
	@Inject @Optional
	public void reactOnEvent(@UIEventTopic("topicTest") String s) {
		label.setText(s);
	}
	
	
	
	
	
	
	
}