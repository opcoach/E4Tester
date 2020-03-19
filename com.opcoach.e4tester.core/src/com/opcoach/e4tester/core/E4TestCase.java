package com.opcoach.e4tester.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.E4Application;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.opcoach.e4tester.core.stubs.E4TesterLogger;

/**
 * This basic class can be used as parent class for any E4 POJO test It
 * initializes osgi and MApplication context so as to be able to test the POJO
 * It also provides a basic application (with nothing inside), in case it is
 * injected in some POJOs. All methods provided in this class are protected and
 * can be overridden
 * 
 * @author olivier
 *
 */
public abstract class E4TestCase extends E4ApplicationDriver {
	

	/** This global setup initializes basic contexts for tests */
	@SuppressWarnings("restriction")
	@BeforeAll
	public static void globalSetup() throws Exception {

		if (e4Appli == null) {
			e4Appli = E4TesterApplication.getE4Appli();
			e4workbench = E4TesterApplication.getE4workbench();

			MApplication appli = e4workbench.getApplication();

			EPartService ps = appli.getContext().get(EPartService.class);
			EModelService ms = appli.getContext().get(EModelService.class);

			// Activate first window (to avoid No Active context !).
			appli.getChildren().get(0).getContext().activate();

			// create E4TesterLogger
			e4testLogger = ContextInjectionFactory.make(E4TesterLogger.class, appli.getContext());
			appli.getContext().set(E4TesterLogger.E4TEST_LOGGER, e4testLogger);

//			// get event broker to subscribe 
//			EventBroker broker = appli.getContext().getLocal(EventBroker.class);
//			
//			// subscribe to LifeCycle Event topic 
//			broker.subscribe(UIEvents.UILifeCycle.ACTIVATE, (eventHandler) -> {
//				
//				// this Event Tag is published when a part is activated
//				if ( eventHandler.containsProperty(EventTags.ELEMENT) && 
//						eventHandler.getProperty(EventTags.ELEMENT) instanceof MPart)
//				{
//					MPart activatePart = (MPart) eventHandler.getProperty(EventTags.ELEMENT);
//					String createdPartId = refPartId.get(); 
//					// get part id and compare with referenced part Id  
//					if ( createdPartId != null && createdPartId.equals(activatePart.getElementId()))
//					{
//						partActivated.set(true);
//						refPartId.set("");
//					}
//					
//				}
//			});
		}

	}

	/**
	 * Release the main test window
	 */
	@AfterEach
	public void release() {
		cleanSelection();
		cleanTestWindow();
	}



}
