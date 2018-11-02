package com.opcoach.e4tester.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.E4Application;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

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

	/** This global setup initializes basic contexts for tests */
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

		}

	}

	/**
	 * Release the main test window
	 */
	@AfterEach
	public void release() {
		cleanTestWindow();
		cleanSelection();
		wait1second();
	}

	private void cleanSelection() {
		getSelectionService().setSelection("");

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
		//  Use a ChangeRecorder EMF to revert the changes ? 
		//  Reload the applicationE4Xmi and all fragments.. but heavy... 
		// It must also ensure that Pojo of part are correctly unregistered from injector. 
		
		
		
		// Must release pojo in each part in partStack
//		for (MStackElement mse : getPartStack().getChildren()) {
//			if (mse instanceof MPart) {
//				// Release the pojo in injector
//				MPart p = (MPart) mse;
//				ContextInjectionFactory.uninject(p.getObject(), p.getContext());
//			}
//
//		} 

		getPartStack().getChildren().clear();
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

		MPart p = null;
		try {

			EPartService ps = getPartService();
			p = ps.createPart(partDescId);

			// Add this part in the test window and activate it !
			MPartStack mps = getPartStack();
			mps.getChildren().add(p);
			p.setOnTop(true);

			ps.showPart(p, PartState.CREATE);
			ps.activate(p);

		} catch (Exception t) {
			t.printStackTrace();
		}
		return p;
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

		} catch (Exception t) {
			t.printStackTrace();
		}
		return p;

	}

	private MPartStack getPartStack() {
		MWindow testWindow = getTestWindow();

		return (MPartStack) testWindow.getChildren().get(0);
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
		Class<?> c = pojo.getClass();
		String result = null;
		try {
			// Get the instance value .
			Object o = getInstanceValue(pojo, widgetFieldName);

			// Look for the getText method in the field instance
			Class<?> wclass = o.getClass(); // Class for this widget
			// Is there a getText method ?
			Method m = wclass.getMethod("getText");
			if (m != null)
				result = (String) m.invoke(o);

		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("The method getText could not be called on value instance of field " + widgetFieldName
					+ " of class : " + c.getCanonicalName());
		} catch (NoSuchMethodException e) {
			System.out.println("The method getText could not be found on value instance of field '" + widgetFieldName
					+ "' in class : " + c.getCanonicalName());
		}

		return result;

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
				result = (boolean) m.invoke(o);

		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("The method getSelection could not be called on value instance of field "
					+ widgetFieldName + " of class : " + c.getCanonicalName());
		} catch (NoSuchMethodException e) {
			System.out.println("The method getSelection could not be found on value instance of field '"
					+ widgetFieldName + "' in class : " + c.getCanonicalName());
		}

		return result;

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
		TreeViewer result = null;

		Object fieldValue = getInstanceValue(pojo, fieldName);
		if (fieldValue != null) {
			if (fieldValue instanceof TreeViewer) {
				result = (TreeViewer) fieldValue;
			} else {
				WrongFieldTypeException wfte = new WrongFieldTypeException(fieldName, pojo, TreeViewer.class,
						fieldValue.getClass());
				System.out.println(wfte.getMessage());
				throw wfte;
			}

		}
		return result;
	}

	protected TreeViewer getTreeViewer(MPart part, String fieldName) {
		getPartService().activate(part);
		return getTreeViewer(part.getObject(), fieldName);
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
			tv.getTree().setFocus();
			// tv.setSelection(new TreeSelection(new TreePath( new Object[] {expected} )));

			// tv.setSelection(new TreeSelection(new TreePath( new Object[] {value} )));
			tv.setSelection(new StructuredSelection(value), true);
		}

	}

	protected void selectObjectInTreeViewer(MPart part, String fieldName, Object value) {

		getPartService().activate(part, true);
		selectObjectInTreeViewer(part.getObject(), fieldName, value);

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
