package com.opcoach.e4tester.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.internal.workbench.swt.E4Application;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;

import com.opcoach.e4tester.core.stubs.E4TesterApplicationContext;

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
public abstract class E4BaseTester {

	private static final String TEST_WINDOW_ID = "com.opcoach.e4tester.core.testWindow";
	private static final String TEST_PART_STACK = "com.opcoach.e4tester.core.partstack";
	
	
	
	protected static E4Application e4Appli = null;
	protected static E4Workbench e4workbench = null;

	/** This global setup initializes basic contexts for tests */
	@BeforeClass // See issue #3 (https://github.com/opcoach/E4Tester/issues/3), replace with
					// BeforeAll later
	public static void globalSetup() throws Exception {

		System.out.println("Enter in globalSetup ");

		if (e4Appli == null)
		{
			e4Appli = E4TesterApplication.getE4Appli();
			e4workbench = E4TesterApplication.getE4workbench();
		}
		

	}

	private static void createE4Application() {
		if (e4Appli != null)
			return;

		e4Appli = new E4Application();

		Display display = e4Appli.getApplicationDisplay();

		e4workbench = e4Appli.createE4Workbench(new E4TesterApplicationContext(), display);

		// e4workbench.initializeContext(E4Workbench.getServiceContext(),
		// e4workbench.getApplication());

		/*
		 * IEclipseContext ctx = e4workbench.getContext(); PartRenderingEngine pre = new
		 * PartRenderingEngine(null); ctx.set(IPresentationEngine.class, pre);
		 */

		// ContextInjectionFactory.inject(pre, ctx);

		// Location instanceLocation = (Location)
		// e4workbench.getContext().get(E4Workbench.INSTANCE_LOCATION);
		Shell shell = display.getActiveShell();
		if (shell == null) {
			shell = new Shell();
			// place it off so it's not visible
			shell.setLocation(0, 10000);
		}

		class RunUI extends Thread {
			E4Workbench e4w;

			public RunUI(E4Workbench e4workbench) {
				super("Run Test UI");
				e4w = e4workbench;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				e4w.createAndRunUI(e4workbench.getApplication());
			}
		}
		Runnable r = new RunUI(e4workbench);
		r.run();

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
			//getModelService().bringToTop(result);
			//getApplication().setSelectedElement(result);
			//result.setOnTop(true);
			// result.getContext().get(EPartService.class);
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
		// getPartStack().getChildren().clear();
	}

	protected EPartService getPartService() {
		return getContext().get(EPartService.class);
	}

	protected EModelService getModelService() {
		return getContext().get(EModelService.class);
	}

	
	class PartCreatorRunner implements Runnable
	{
		MPart createdPart = null; 
		String name = "";
		Class<?> pojoClazz = null;
		
		
		public PartCreatorRunner(String name, Class<?> pojoClazz) {
			this.name = name;
			this.pojoClazz = pojoClazz;
		}
		
		public MPart getCreatedPart() {
			return createdPart;
		}
		public void run() {

			// TODO Auto-generated method stub
			 MPart p = getModelService().createModelElement(MPart.class);
			p.setLabel(name);
			p.setContributionURI("bundleclass://" + pojoClazz.getCanonicalName());
			p.setVisible(true);
			p.setToBeRendered(true);
			
			Object o = ContextInjectionFactory.make(pojoClazz, getContext());
			p.setObject(o);

			// Add this part in the test window and activate it !
			MPartStack mps = getPartStack();
			mps.getChildren().add(p);
			p.setOnTop(true);

			// getTestWindow().setOnTop(true);

			createdPart = p;
			getPartService().showPart(p, PartState.CREATE);
			getPartService().activate(p);

			/*
			 * IEclipseContext partContext = EclipseContextFactory.create("Context for " +
			 * clazz.getName()); // partContext.setParent(ctx); Shell s = getShell();
			 * partContext.set(Composite.class, s); Object result =
			 * ContextInjectionFactory.make(clazz, partContext);
			 * 
			 * s.pack(); s.open();
			 */
		
		}
	}
	
	/**
	 * Create a part in the test window
	 * 
	 * @param clazz
	 * @return
	 */
	public MPart createTestPart(String name, Class<?> pojoClazz) {
		
		MPart p= null;
		try {
		//PartCreatorRunner pcr = new PartCreatorRunner(name, pojoClazz);
		//Display.getDefault().syncExec(pcr);
		 p = getModelService().createModelElement(MPart.class);
		p.setLabel(name);
		p.setContributionURI("bundleclass://" + pojoClazz.getCanonicalName());
		p.setVisible(true);
		p.setToBeRendered(true);
		

		// Add this part in the test window and activate it !
		MPartStack mps = getPartStack();
		mps.getChildren().add(p);
		p.setOnTop(true);

		// getTestWindow().setOnTop(true);
		getPartService().showPart(p, PartState.CREATE);
		getPartService().activate(p);
		
		Object o = ContextInjectionFactory.make(pojoClazz, p.getContext());
	p.setObject(o);

	

		
		
		
		
		wait1second();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return p;

	}

	private MPartStack getPartStack() {
		MWindow testWindow = getTestWindow();

		MPartStack mps = (MPartStack) testWindow.getChildren().get(0);
		return mps;
	}

	protected Shell getTestShell() {
		return (Shell) getTestWindow().getWidget();
	}

	protected static IEclipseContext getContext() {
		return e4workbench.getContext();
	}

	public void wait1second() {
		try {
			Thread.sleep(100L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public static void openContextSpy() {
	 * System.out.println("Opening SpyContext"); Display d = Display.getCurrent();
	 * Shell s = new Shell(d); s.setLayout(new GridLayout(1, true));
	 * s.setText("Context Spy"); IEclipseContext spyCtx =
	 * EclipseContextFactory.create("SpyContext"); spyCtx.setParent(ctx);
	 * spyCtx.set(Composite.class, s); ContextSpyPart cs =
	 * ContextInjectionFactory.make(ContextSpyPart.class, spyCtx);
	 * 
	 * Job j = new Job("UI") { protected IStatus run(IProgressMonitor monitor) {
	 * s.pack(); s.open();
	 * 
	 * while (!s.isDisposed()) { if (!d.readAndDispatch()) d.sleep(); }
	 * 
	 * System.out.println("End Opening SpyContext"); return Status.OK_STATUS;
	 * 
	 * } }; j.schedule(); }
	 */

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
	 * Get the value of a field in the pojo instance
	 * 
	 * @param pojo
	 *            : the pojo instance
	 * @param fieldName
	 *            : the name of the field in the pojo's class
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

		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("The  field name " + fieldName + " does not exist in the class " + c.getCanonicalName());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.out.println(
					"The field value of '" + fieldName + "' can not be extracted from class : " + c.getCanonicalName());
		}

		return result;

	}

	/**
	 * Get the treeviewer instance stored in the field 'fieldname' of the pojo
	 * instance.
	 * 
	 * @param pojo
	 *            the part to be analyzed
	 * @param fieldName
	 *            the fieldname containing the tree viewer
	 * @return the treeviewer of null if nothing found.
	 */
	protected TreeViewer getTreeViewer(Object pojo, String fieldName) {
		TreeViewer result = null;

		Object fieldValue = getInstanceValue(pojo, fieldName);
		if ((fieldValue != null) && (fieldValue instanceof TreeViewer)) {
			result = (TreeViewer) fieldValue;
		}
		return result;
	}

	/**
	 * 
	 * @param pojo
	 * @param fieldName
	 * @param value
	 */
	protected void selectObjectInTreeViewer(Object pojo, String fieldName, Object value) {

		// Then can select value in the treeviewer
		TreeViewer tv = getTreeViewer(pojo, fieldName);
		if (tv != null) {
			tv.setSelection(new StructuredSelection(value));
		}

	}

}
