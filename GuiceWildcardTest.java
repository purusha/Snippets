import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class GuiceWildcardTest {

    @Test 
    public void a() {
                
        final Injector injector = Guice.createInjector(new AbstractModule() {
            @Override protected void configure() {
                bind(new TypeLiteral<Class<? extends SuperClass>>() {}).toInstance(SubClass.class);
            }
        });
        
        final ClassHolder holder = new ClassHolder();
        injector.injectMembers(holder);        
        assertEquals(SubClass.class, holder.type);
    }

    static class SuperClass {}

    static class SubClass extends SuperClass {}

    static class ClassHolder {
        @Inject Class<? extends SuperClass> type;
    }
    
    @Test
    public void b() {
    	
		final Injector injector = Guice.createInjector(new AbstractModule(){
			@Override
			protected void configure() {				
				bind(new TypeLiteral<Service<Class<?>>>(){});				
			}
		});
		
		TypeLiteral<Service<Bean1>> s1literal = new TypeLiteral<Service<Bean1>>(){};
		Service<Bean1> s1 = injector.getInstance(Key.get(s1literal));
		assertTrue(s1literal.toString().contains(s1.type.toString()));
		
		TypeLiteral<Service<Bean3>> s3literal = new TypeLiteral<Service<Bean3>>(){};
		Service<Bean3> s3 = injector.getInstance(Key.get(s3literal));
		assertTrue(s3literal.toString().contains(s3.type.toString()));		
    	
    }
    
	public static class MyBean { }
	public static class Bean1 extends MyBean { }
	public static class Bean3 {}

	public static class Service<T> {		
		TypeLiteral<T> type;

		@Inject
		public Service(TypeLiteral<T> type){
			this.type = type;
		}		
	}
    
}