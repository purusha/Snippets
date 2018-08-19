import java.util.Collection;
import java.util.concurrent.Callable;

public class GenericsExample3 {
    public static void main(String[] args) {
        
    }
    
    static class MyExecutor<T> {        
        public void executeAll(Collection<Callable<T>> tasks) {
            
        }        
    }
    
    static class MyExecutor2 {        
        public <T> void executeAll(Collection<Callable<T>> tasks) {
            
        }        
    }
}    
