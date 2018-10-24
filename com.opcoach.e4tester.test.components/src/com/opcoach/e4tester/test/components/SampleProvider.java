package com.opcoach.e4tester.test.components;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;

public class SampleProvider extends LabelProvider implements ITreeContentProvider {

	private int level = 0; 
	
	@Override
	public Object[] getElements(Object inputElement) {
		return new String[] { "String1", "String2"};
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof String)
		{
			// Get the last digit (x) and generate 'Stringx1' ... 
			String p = (String) parentElement;
			String last = p.substring(p.length()-1);
			String result[] = new String[5];
			for (int i = 0; i < 5; i++)
				result[i] = p + i;
			
			level++;
			return result;
		}
			
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof String) {
			return ((String) element).substring(0,((String) element).length()-2);
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// Always have children
		return level < 5;
	}

}
