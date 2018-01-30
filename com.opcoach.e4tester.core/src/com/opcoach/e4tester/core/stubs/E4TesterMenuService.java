package com.opcoach.e4tester.core.stubs;

import org.eclipse.e4.ui.services.EMenuService;

public class E4TesterMenuService implements EMenuService {

	@Override
	public boolean registerContextMenu(Object parent, String menuId) {
		return true;
	}

}
