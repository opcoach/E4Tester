 
package com.opcoach.e4tester.core.test.components;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/** Create a sample part with label and treeviewer */
public class Part1 {
	
	public static final String ID = "com.opcoach.e4tester.core.test.part1";
	public static final String LABEL_VALUE = "a value in label";
	
	private Label label;
	private TreeViewer tv;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setText(LABEL_VALUE);
		
		tv = new TreeViewer(parent);
		SampleProvider p = new SampleProvider();
		tv.setLabelProvider(p);
		tv.setContentProvider(p);
		tv.setInput("");
	}
	
	@Inject @Optional
	public void reactOnEvent(@UIEventTopic("topicTest") String s) {
		label.setText(s);
	}
	
	
	
	
	
	
	
}