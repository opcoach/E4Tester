 
package com.opcoach.e4tester.test.components;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/** Create a sample part with label and treeviewer */
public class Part1 {
	
	public static final String ID = "com.opcoach.e4tester.test.components.part1";
	public static final String LABEL_VALUE = "a value in label";
	public static final String CHECKBOX_SELECTED_TEXT = "checkboxSelectedText";
	public static final String CHECKBOX_NOTSELECTED_TEXT = "checkboxNotSelectedText";
	
	private Label label;
	private Button checkboxSelected;
	private Button checkboxNotSelected;
	private Combo combobox;
	private TreeViewer tv;
	
	@PostConstruct
	public void postConstruct(Composite parent, ESelectionService selService) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		
		Composite left = new Composite(parent, SWT.NONE);
		left.setLayout(new FillLayout());
		
		// Manage left part
				SampleProvider p = new SampleProvider();

				tv = new TreeViewer(left);
				tv.setLabelProvider(p);
				tv.setContentProvider(p);
				tv.setInput("");
				tv.addSelectionChangedListener((e) -> {
					IStructuredSelection iss = (IStructuredSelection) e.getSelection();
					selService.setSelection(iss.size() == 1 ? iss.getFirstElement() : iss.toArray());
					
				});
				tv.expandToLevel(2);
		
				
				// Manage right part
		Composite right = new Composite(parent, SWT.NONE);
		right.setLayout(new GridLayout(1, false));
		
		label = new Label(right, SWT.NONE);
		label.setText(LABEL_VALUE);
		
		checkboxSelected = new Button(right, SWT.CHECK);
		checkboxSelected.setText(CHECKBOX_SELECTED_TEXT);
		checkboxSelected.setSelection(true);
		
		checkboxNotSelected = new Button(right, SWT.CHECK);
		checkboxNotSelected.setText(CHECKBOX_NOTSELECTED_TEXT);
		checkboxNotSelected.setSelection(false);
		
		combobox = new Combo(right, SWT.NONE);
		
		combobox.setItems("Combo1","Combo2","Combo3","Combo4","Combo5","Combo6");
		combobox.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selService.setSelection(combobox.getText());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	@Inject @Optional
	public void reactOnEvent(@UIEventTopic("topicTest") String s) {
		label.setText(s);
	}
	
	@PreDestroy
	public void predest(MPart p)
	{
		System.out.println("Enter in predestroy for part " + p.getLabel());
	}
	
	
	
	
	
}