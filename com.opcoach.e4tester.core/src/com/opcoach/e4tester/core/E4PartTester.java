package com.opcoach.e4tester.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.tools.context.spy.ContextSpyPart;
import org.eclipse.e4.ui.internal.workbench.PartServiceImpl;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;

import com.opcoach.e4tester.core.stubs.DisplayRealm;
import com.opcoach.e4tester.core.stubs.E4TesterMenuService;

/** This basic class can be used as parent class for any E4 Part test
 *  It provides a basic mechanism for selection and recreate part context for each part creation.
 *  All methods provided in this class are protected and can be overriden  
 * @author olivier
 *
 */
public class E4PartTester extends E4BaseTester {

	
	@BeforeClass   // See issue #3 (https://github.com/opcoach/E4Tester/issues/3), replace with BeforeAll later
	public static void initForParts() throws Exception
	{
		System.out.println("Enter in initForParts ");
		
		// Init databinding realm
		Realm realm = DisplayRealm.getRealm(Display.getDefault());
		DisplayRealm.initDefault(realm);
		ctx.set(Realm.class, realm);
		
		// Init menu service for popup menus
		EMenuService menuserv = new E4TesterMenuService();
		ctx.set(EMenuService.class, menuserv);
		
		EPartService ps = new PartServiceImpl(ctx.get(MApplication.class), null );
		ctx.set(EPartService.class,  ps);


	}
	
	protected static Composite getParentComposite()
	{
		return getShell();
	}
	
	/** Create a part in a subcontext with a specific shell 
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T createPojoPart(Class<T> clazz)
	{
		IEclipseContext partContext =  EclipseContextFactory.create("Context for " + clazz.getName());
		partContext.setParent(ctx);
		Shell s = getShell();
		partContext.set(Composite.class, s);
		Object result = ContextInjectionFactory.make(clazz, partContext);
		
		s.pack();
		s.open();
		
		return (T) result;
		
	}
	
	public static void openContextSpy()
	{
		System.out.println("Opening SpyContext");
		
		ContextSpyPart cs = createPojoPart(ContextSpyPart.class);
	
		
		System.out.println("End Opening SpyContext");

		
	}
	
	
	/** Get the text value of the clazz widget, null if no field found or no getText method */
	public String getTextWidgetValue(Object pojo, String widgetFieldName)
	{
		// Get the field in the pojo object
		Class<?> c = pojo.getClass();
		String result = null; 
		try {
			// Get the instance value .
			Object o = getInstanceValue(pojo, widgetFieldName);
					
			// Look for the getText method in the field instance
			Class<?> wclass = o.getClass(); // Class for this widget
			// Is there a getText method ? 
			Method  m = wclass.getMethod("getText");
			if (m != null)
				result = (String) m.invoke(o);
			
			
		} catch (  IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("The method getText could not be called on value instance of field " + widgetFieldName + " of class : " + c.getCanonicalName());
		}
		catch ( NoSuchMethodException e) {
			System.out.println("The method getText could not be found on value instance of field '" + widgetFieldName + "' in class : " + c.getCanonicalName());
		}
		
		
		return result; 
		
	}
	
	/**
	 * Get the value of a field in the pojo instance
	 * @param pojo : the pojo instance
	 * @param fieldName : the name of the field in the pojo's class
	 * @return
	 */
	protected Object getInstanceValue(Object pojo, String fieldName) {
		// Get the field in the pojo object
		Class<?> c = pojo.getClass();
		Object  result = null; 
		try {
			Field f = c.getDeclaredField(fieldName);
			
			f.setAccessible(true);

			// Get the instance value .
			result = f.get(pojo);
			
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("The  field name " + fieldName  + " does not exist in the class " + c.getCanonicalName());
		}catch ( IllegalArgumentException | IllegalAccessException e) {
			System.out.println("The field value of '" + fieldName + "' can not be extracted from class : " + c.getCanonicalName());
		}
		
		return result;
		
	}
	

	/**
	 * Get the treeviewer instance stored in the field 'fieldname' of the  pojo instance.
	 * @param pojo the part to be analyzed
	 * @param fieldName the fieldname containing the tree viewer
	 * @return the treeviewer of null if nothing found.
	 */
	protected TreeViewer getTreeViewer(Object pojo, String fieldName) {
		TreeViewer result = null;
		
		Object fieldValue = getInstanceValue(pojo, fieldName);
		if ((fieldValue != null) && (fieldValue instanceof TreeViewer))
		{
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
	protected void selectObjectInTreeViewer(Object pojo, String fieldName, Object value)
	{
		
		// Then can select value in the treeviewer
		TreeViewer tv = getTreeViewer(pojo, fieldName);
		if (tv != null) {
			tv.setSelection(new StructuredSelection(value));
		}
		
	}
}
