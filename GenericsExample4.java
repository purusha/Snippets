import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

public class GenericsExample4 {
    
    /*
     *  upper bound generics type parameter  
     */
    
    public static void main(String[] args) {                
        new MyExecutor<String>().executeAll(
            new ArrayList<Callable<String>>()
        );
        
        new MyExecutor<String>().executeAll(
            new ArrayList<MyTask>()
        );
        
        //compile error
//        new MyExecutor<String>().executeAll(
//            new ArrayList<Callable<Number>>()
//        );         
    }
    
    class MyTask implements Callable<String> {
        @Override
        public String call() throws Exception {
            return null;
        }
    }
    
    static class MyExecutor<T> {
        
        //does not compile !!?
        //public void executeAll(Collection<Callable<T>> tasks) {}
        
        public void executeAll(Collection<? extends Callable<T>> tasks) {}
        
    }
}    