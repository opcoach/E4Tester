package com.opcoach.e4tester.core.stubs;

import org.eclipse.core.runtime.Adapters;
import org.eclipse.e4.core.services.adapter.Adapter;

public class EclipseAdapter extends Adapter {

	@Override
	public <T> T adapt(Object element, Class<T> adapterType) {
		return Adapters.adapt(element, adapterType);
	}

}