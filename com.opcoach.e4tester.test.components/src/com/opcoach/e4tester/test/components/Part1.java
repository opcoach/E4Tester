 
package com.opcoach.e4tester.test.components;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/** Create a sample part with label and treeviewer */
public class Part1 {
	
	public static final String ID = "com.opcoach.e4tester.test.part1";
	public static final String LABEL_VALUE = "a value in label";
	public static final String CHECKBOX_SELECTED_TEXT = "checkboxSelectedText";
	public static final String CHECKBOX_NOTSELECTED_TEXT = "checkboxNotSelectedText";
	
	private Label label;
	private Button checkboxSelected;
	private Button checkboxNotSelected;
	private TreeViewer tv;
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setText(LABEL_VALUE);
		
		checkboxSelected = new Button(parent, SWT.CHECK);
		checkboxSelected.setText(CHECKBOX_SELECTED_TEXT);
		checkboxSelected.setSelection(true);
		
		checkboxNotSelected = new Button(parent, SWT.CHECK);
		checkboxNotSelected.setText(CHECKBOX_NOTSELECTED_TEXT);
		checkboxNotSelected.setSelection(false);
		
		
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