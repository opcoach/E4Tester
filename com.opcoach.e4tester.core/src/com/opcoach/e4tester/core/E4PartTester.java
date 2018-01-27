package com.opcoach.e4tester.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.tools.context.spy.ContextSpyPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.BeforeClass;

import com.opcoach.e4tester.core.stubs.DisplayRealm;

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
			Field f = c.getDeclaredField(widgetFieldName);
			
			f.setAccessible(true);

			// Get the instance value .
			Object o = f.get(pojo);
			// Look for the getText method in the field instance
			Class<?> wclass = o.getClass(); // Class for this widget
			// Is there a getText method ? 
			Method  m = wclass.getMethod("getText");
			if (m != null)
				result = (String) m.invoke(o);
			
			
		} catch (NoSuchFieldException | SecurityException e) {
			System.out.println("The widget field name " + widgetFieldName  + " does not exist in the class " + c.getCanonicalName());
		}catch ( NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			System.out.println("The method getText was not found or could not be called on widget " + widgetFieldName + " of class : " + c.getCanonicalName());
		}
		
		
		return result; 
		
	}
}
