
package com.opcoach.e4tester.test.components;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Create a sample part with a label updated when selection in tree of Part 1
 * changes
 */
public class Part2 {
	public static final String ID = "com.opcoach.e4tester.test.components.part2";

	private Label label;

	@PostConstruct
	public void postConstruct(Composite parent) {
		label = new Label(parent, SWT.NONE);
		label.setText(" sample label ");
	}

	@Inject
	@Optional
	public void reactOnSelection(@Named(IServiceConstants.ACTIVE_SELECTION) String s) {
		if ((label != null) && !label.isDisposed())
			label.setText(s);
	}

}