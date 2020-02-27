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
public abstract class E4TestCase {

	private static final String TEST_WINDOW_ID = "com.opcoach.e4tester.core.testWindow";
	private static final String TEST_PART_STACK = "com.opcoach.e4tester.core.partstack";

	protected static E4Application e4Appli = null;
	protected static E4Workbench e4workbench = null;
	
	
	protected static E4TesterLogger e4testLogger = null;

	static AtomicBoolean partActivated = new AtomicBoolean(false);

	static AtomicReference<String> refPartId = new AtomicReference<>();

	static long DEFAULT_TIMEOUT_PART_ACTIVATION = 2 * 1000;

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

	private void cleanSelection() {
		getSync().syncExec(() -> {
			getSelectionService().setSelection("");
		});
	}

	/** Create or return the test Window as a sibling of application's window. */
	protected MWindow getTestWindow() {
		MApplication a = getApplication();
		MWindow result = null;
		// Search for Test window.
		Optional<MWindow> tw = a.getChildren().stream().filter(w -> TEST_WINDOW_ID.equals(w.getElementId()))
				.findFirst();
		if (tw.isPresent()) {
			result = tw.get();
			result.getContext().activate();
			getApplication().setSelectedElement(result);

		}

		return result;

	}

	public MApplication getApplication() {
		return e4workbench.getApplication();
	}

	/**
	 * This method removes the content in the test window (created by previous test)
	 */
	protected void cleanTestWindow() {

		// Must ensure that Test model is always the same for each test..
		// 2 ideas :
		// Use a ChangeRecorder EMF to revert the changes ?
		// Reload the applicationE4Xmi and all fragments.. but heavy...
		// It must also ensure that Pojo of part are correctly unregistered from
		// injector.

		// Must release pojo in each part in partStack
		getSync().syncExec(() -> {
			
			Object[] toBeDeleted = getPartStack().getChildren().toArray();
			for (Object o : toBeDeleted)
				getModelService().deleteModelElement((MStackElement) o);
		});

	}

	protected EPartService getPartService() {
		return getContext().get(EPartService.class);
	}

	protected IEventBroker getEventBroker() {
		return getContext().get(IEventBroker.class);
	}

	protected EModelService getModelService() {
		return getContext().get(EModelService.class);
	}
	
	protected UISynchronize getSync() {
		return getContext().get(UISynchronize.class);
	}

	protected ESelectionService getSelectionService() {
		return getContext().get(ESelectionService.class);
	}

	/**
	 * Create a part from the corresponding part Descriptor defined.
	 * 
	 * @param partDescId
	 * @return
	 */
	public MPart createTestPart(String partDescId) {
		AtomicReference<MPart> refpart = new AtomicReference<>();
		getSync().syncExec(() -> {
			MPart p = null;
			try {

				// refPartId.set(partDescId);
				EPartService ps = getPartService();
				p = ps.createPart(partDescId);

				// Add this part in the test window and activate it !
				MPartStack mps = getPartStack();
				mps.getChildren().add(p);
				p.setOnTop(true);

				ps.showPart(p, PartState.CREATE);
				ps.activate(p);
				// waitActivatedPart();
				refpart.set(p);
			} catch (Exception t) {
				e4testLogger.error(t);
			}
			// return p;

		});
		return refpart.get();
	}

	/**
	 * Create a part in the test window using raw parameters If a part descriptor is
	 * already defined use rather createTestPart(String partDescId)
	 * 
	 * @param name  : the name for this part
	 * @param id    : the ID of created Part
	 * @param clazz
	 * @return
	 */
	public MPart createTestPart(String name, String id, Class<?> pojoClazz) {
		AtomicReference<MPart> refpart = new AtomicReference<>();

		getSync().syncExec(() -> {
			MPart p = null;
			try {
				p = getModelService().createModelElement(MPart.class);
				p.setLabel(name);
				Bundle b = FrameworkUtil.getBundle(pojoClazz);
				p.setContributionURI("bundleclass://" + b.getSymbolicName() + "/" + pojoClazz.getCanonicalName());
				p.setVisible(true);
				p.setElementId(id);
				p.setToBeRendered(true);

				// Add this part in the test window and activate it !
				MPartStack mps = getPartStack();
				mps.getChildren().add(p);
				p.setOnTop(true);

				getPartService().showPart(p, PartState.CREATE);
				getPartService().activate(p);
				refpart.set(p);
			} catch (Exception t) {
				t.printStackTrace();
			}
		});
		return refpart.get();

	}
	
	protected void disposeTestPart(MPart p)
	{
		
	}

	private void waitActivatedPart() {
		getSync().syncExec(() -> {

			long end = System.currentTimeMillis() + DEFAULT_TIMEOUT_PART_ACTIVATION;
			while (!partActivated.get()) {
				try {
					Thread.sleep(200L);
					if (System.currentTimeMillis() > end) {
						partActivated.set(true);
					}

				} catch (InterruptedException e) {
					e4testLogger.error(e);
				}
			}
		});
		partActivated.set(false);
	}

	private MPartStack getPartStack() {
		MWindow testWindow = getTestWindow();
		MPartStack ps = null;
		if (testWindow.getChildren().size() == 0) {
			ps = getModelService().createModelElement(MPartStack.class);
			testWindow.getChildren().add(ps);
		}
		ps = (MPartStack) testWindow.getChildren().get(0);

		return ps;

	}

	protected Shell getTestShell() {
		return (Shell) getTestWindow().getWidget();
	}

	protected static IEclipseContext getContext() {
		return e4workbench.getContext();
	}

	public void wait1second() {
		waitseconds(1);
	}

	public static void waitseconds(int nbSec) {
		waitseconds(null, nbSec);
	}

	public static void waitseconds(String mess, int nbSec) {

		try {
			if (mess != null) {
				for (int i = 0; i < nbSec; i++) {
					if (nbSec > 1) {
						if (mess != null)
							System.out.println(mess + " " + i + "/" + nbSec);
						else
							System.out.println("Waiting " + i + "/" + nbSec);
					}

					Thread.sleep(1000L);
				}
			} else
				Thread.sleep(nbSec * 500);

		} catch (

		InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the text value in the field defined in the Pojo contained in the part
	 * 
	 * @param part            the part to be tested
	 * @param widgetFieldName the name of the field in the relative pojo class
	 * @return
	 * @throws NoSuchFieldException
	 */
	public String getTextWidgetValue(MPart part, String widgetFieldName) {
		Object pojo = (part == null) ? null : part.getObject();
		return (pojo == null) ? null : getTextWidgetValue(pojo, widgetFieldName);
	}

	/**
	 * Get the text value of the clazz widget, null if no field found or no getText
	 * method
	 */
	public String getTextWidgetValue(Object pojo, String widgetFieldName) {
		// Get the field in the pojo object
		AtomicReference<String> refText = new AtomicReference<>();
		getSync().syncExec(() -> {
			Class<?> c = pojo.getClass();
			Object o = null;
			try {
				// Get the instance value .
				o = getInstanceValue(pojo, widgetFieldName);

				// Look for the getText method in the field instance
				Class<?> wclass = o.getClass(); // Class for this widget
				// Is there a getText method ?
				Method m = wclass.getMethod("getText");
				if (m != null) {
						refText.set((String) m.invoke(o));
				}

			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				System.out.println("The method getText could not be called on value instance of field "
						+ widgetFieldName + " of class : " + c.getCanonicalName());
			} catch (NoSuchMethodException e) {
				System.out.println("The method getText could not be found on value instance of field '"
						+ widgetFieldName + "' in class : " + c.getCanonicalName());
			} 
		});
		return refText.get();

	}

	/**
	 * Get the text value of the clazz widget, null if no field found or no getText
	 * method
	 */
	synchronized public void setTextWidgetValue(Object pojo, String widgetFieldName, String newValue) {
	
		getSync().syncExec(() -> {

				// Get the field in the pojo object
			Class<?> c = pojo.getClass();
			String result = null;
			Object o = null;
			try {
				// Get the instance value .
				o = getInstanceValue(pojo, widgetFieldName);

				// Look for the getText method in the field instance
				Class<?> wclass = o.getClass(); // Class for this widget
				// Is there a getText method ?
				Method m = wclass.getMethod("setText", String.class);
				if (m != null) {
						m.invoke(o, newValue);
				}

			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				System.out.println("The method getText could not be called on value instance of field "
						+ widgetFieldName + " of class : " + c.getCanonicalName());
			} catch (NoSuchMethodException e) {
				System.out.println("The method getText could not be found on value instance of field '"
						+ widgetFieldName + "' in class : " + c.getCanonicalName());
			} 
		});
	}

	/**
	 * Check whether the button loaded in widgetFieldName is selected in the part
	 * 
	 * @param part            the part to be tested
	 * @param widgetFieldName the field name in the pojo that contains the widget
	 * @return
	 */
	public boolean isButtonChecked(MPart part, String widgetFieldName) {
		Object pojo = (part == null) ? null : part.getObject();
		return (pojo == null) ? false : isButtonChecked(pojo, widgetFieldName);

	}

	/**
	 * Check whether the button loaded in widgetFieldName is selected
	 * 
	 * @param pojo            the pojo containing the widget
	 * @param widgetFieldName the field name in the pojo that contains the widget
	 * @return
	 */
	public boolean isButtonChecked(Object pojo, String widgetFieldName) {
		// Get the field in the pojo object
		AtomicBoolean abool = new AtomicBoolean();
		getSync().syncExec(() -> {
			Class<?> c = pojo.getClass();
			boolean result = false;
			try {
				// Get the instance value .
				Object o = getInstanceValue(pojo, widgetFieldName);

				// Look for the getText method in the field instance
				Class<?> wclass = o.getClass(); // Class for this widget
				// Is there a getText method ?
				Method m = wclass.getMethod("getSelection");
				if (m != null)
					abool.set((boolean) m.invoke(o));

			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
				System.out.println("The method getSelection could not be called on value instance of field "
						+ widgetFieldName + " of class : " + c.getCanonicalName());
			} catch (NoSuchMethodException e) {
				System.out.println("The method getSelection could not be found on value instance of field '"
						+ widgetFieldName + "' in class : " + c.getCanonicalName());
			}

		});
		return abool.get();

	}

	/**
	 * Get the value of a field in the pojo instance
	 * 
	 * @param pojo      : the pojo instance
	 * @param fieldName : the name of the field in the pojo's class
	 * @return
	 */
	protected Object getInstanceValue(Object pojo, String fieldName) {
		// Get the field in the pojo object
		Class<?> c = pojo.getClass();
		Object result = null;
		try {
			Field f = c.getDeclaredField(fieldName);

			f.setAccessible(true);

			// Get the instance value .
			result = f.get(pojo);

		} catch (NoSuchFieldException e) {
			WrongFieldnameException wfe = new WrongFieldnameException(fieldName, pojo);
			System.out.println(wfe.getMessage());
			throw wfe;
		} catch (SecurityException e) {
			System.out
					.println("The field name " + fieldName + " is not accessible in the class " + c.getCanonicalName());
			throw e;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.out.println(
					"The field value of '" + fieldName + "' can not be extracted from class : " + c.getCanonicalName());
		}

		return result;

	}

	protected Object getInstanceValue(MPart part, String fieldName) {
		return getInstanceValue(part.getObject(), fieldName);
	}

	/**
	 * Get the treeviewer instance stored in the field 'fieldname' of the pojo
	 * instance.
	 * 
	 * @param pojo      the part to be analyzed
	 * @param fieldName the fieldname containing the tree viewer
	 * @return the treeviewer of null if nothing found.
	 * @throws NoSuchFieldException
	 */
	protected TreeViewer getTreeViewer(Object pojo, String fieldName) {
		AtomicReference<TreeViewer> refresult = new AtomicReference<>();
		AtomicReference<WrongFieldTypeException> refexeceptionToThrow = new AtomicReference<>();
		getSync().syncExec(() -> {
			Object fieldValue = getInstanceValue(pojo, fieldName);
			if (fieldValue != null) {
				if (fieldValue instanceof TreeViewer) {
					refresult.set((TreeViewer) fieldValue);
				} else {
					WrongFieldTypeException wfte = new WrongFieldTypeException(fieldName, pojo, TreeViewer.class,
							fieldValue.getClass());
					refexeceptionToThrow.set(wfte);
					System.out.println(wfte.getMessage());
				}
			}

		});
		if (refexeceptionToThrow.get() != null)
			throw refexeceptionToThrow.get();
		return refresult.get();
	}

	protected TreeViewer getTreeViewer(MPart part, String fieldName) {
		AtomicReference<TreeViewer> refTreev = new AtomicReference<>();
		AtomicReference<WrongFieldTypeException> refexeceptionToThrow = new AtomicReference<>();
		getSync().syncExec(() -> {
			getPartService().activate(part);
			try {
				TreeViewer treev = getTreeViewer(part.getObject(), fieldName);
				refTreev.set(treev);
			} catch (WrongFieldTypeException ex) {
				refexeceptionToThrow.set(ex);
			}
		});
		if (refexeceptionToThrow.get() != null)
			throw refexeceptionToThrow.get();
		return refTreev.get();
	}
	
	protected Object[] getExpandElements(TreeViewer tv)
	{
		AtomicReference<Object[]> refExpand = new AtomicReference<>();

		getSync().syncExec( () -> { 
			tv.expandAll(); 
			refExpand.set( tv.getExpandedElements());
		});
		return refExpand.get();
	}
	
	protected void expandAll(TreeViewer tv)
	{
		getSync().syncExec(() ->
		tv.expandAll());
	}

	/**
	 * Get the nattable instance stored in the field 'fieldname' of the pojo
	 * instance.
	 * 
	 * @param pojo      the part to be analyzed
	 * @param fieldName the fieldname containing the tree viewer
	 * @return the treeviewer of null if nothing found.
	 * @throws NoSuchFieldException
	 */
	protected NatTable getNatTable(Object pojo, String fieldName) {
		NatTable result = null;

		Object fieldValue = getInstanceValue(pojo, fieldName);
		if (fieldValue != null) {
			if (fieldValue instanceof NatTable) {
				result = (NatTable) fieldValue;
			} else {
				WrongFieldTypeException wfte = new WrongFieldTypeException(fieldName, pojo, TreeViewer.class,
						fieldValue.getClass());
				e4testLogger.error(wfte.getMessage());
				throw wfte;
			}
		}
		return result;
	}

	protected NatTable getNatTable(MPart part, String fieldName) {
		getPartService().activate(part);
		return getNatTable(part.getObject(), fieldName);
	}

	/**
	 * Get the control instance stored in the field 'fieldname' of the pojo
	 * instance.
	 * 
	 * @param pojo         the part to be analyzed
	 * @param fieldName    the fieldname containing the tree viewer
	 * @param controlClass the expectedClass for this control
	 * @return the control of null if nothing found.
	 * @throws NoSuchFieldException
	 */
	protected <T extends Control> T getControl(Object pojo, String fieldName, Class<T> controlClass) {
		T result = null;

		Object fieldValue = getInstanceValue(pojo, fieldName);
		if (fieldValue != null) {
			if (fieldValue.getClass() == controlClass) {
				result = (T) fieldValue;
			} else {
				WrongFieldTypeException wfte = new WrongFieldTypeException(fieldName, pojo, controlClass,
						fieldValue.getClass());
				System.out.println(wfte.getMessage());
				throw wfte;
			}

		}
		return result;
	}

	protected <T extends Control> T getControl(MPart part, String fieldName, Class<T> controlClass) {
		getPartService().activate(part);
		return getControl(part.getObject(), fieldName, controlClass);
	}

	protected Combo getCombo(MPart part, String fieldName) {
		return getControl(part, fieldName, Combo.class);
	}

	protected Text getTextWidget(MPart part, String fieldName) {
		return getControl(part, fieldName, Text.class);
	}

	/**
	 * 
	 * @param pojo
	 * @param fieldName
	 * @param value
	 */
	 protected void selectObjectInTreeViewer(Object pojo, String fieldName, Object value) {

		////// MUST BE UPDATED... IT WORKS IF PARTS ARE CREATED IN A GIVEN ORDER (See
		////// tests)...

		// Then can select value in the treeviewer
		TreeViewer tv = getTreeViewer(pojo, fieldName);
		if (tv != null) {
			getSync().syncExec(() -> { 			tv.getTree().setFocus();
			// tv.setSelection(new TreeSelection(new TreePath( new Object[] {expected} )));

			// tv.setSelection(new TreeSelection(new TreePath( new Object[] {value} )));
			tv.setSelection(new StructuredSelection(value), true);
});
		}

	}

	protected void selectObjectInTreeViewer(MPart part, String fieldName, Object value) {
		getSync().syncExec(() -> {
			getPartService().activate(part, true);
			selectObjectInTreeViewer(part.getObject(), fieldName, value);
		});
	}

	public void setSelectionService(Object selection) {
		getSync().syncExec(() -> {
			getSelectionService().setSelection(selection);
		});

	}

	/// ASSERT METHODS

	/**
	 * 
	 * @param p         : the part containing the widget
	 * @param fieldname : the fieldname in the pojo class that host the label
	 * @param expected  : the expected string in the Label
	 * @param message   : the message to display when it fails
	 */
	public void assertLabelContains(MPart p, String fieldname, String expected, String message) {
		assertEquals(expected, getTextWidgetValue(p, fieldname), message);
	}

	/**
	 * Switch to the perspective and returns it
	 * 
	 * @param id of the perspective to show (can be defined in a snippet fragment)
	 * @return the perspective in the model or null if nothing found or created.
	 */
	public MPerspective showPerspective(String id) {
		EPartService ps = getPartService();
		Optional<MPerspective> p = ps.switchPerspective(id);
		if (p.isPresent())
			return p.get();
		else {
			// Find the perspective in snippet and show it.
			MPerspective pers = (MPerspective) getModelService().findSnippet(getApplication(), id);
			if (pers != null) {
				ps.switchPerspective(pers);
				return pers;
			}
		}

		return null;

	}

}
