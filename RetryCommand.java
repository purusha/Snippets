import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryCommand<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryCommand.class);
    
    private final int maxRetries;
    private final Class<? extends Throwable> retryOn;

    public RetryCommand(int maxRetries, Class<? extends Throwable> retryOn) {
        this.maxRetries = maxRetries;
        this.retryOn = retryOn;
    }
    
    public Optional<T> run(Supplier<T> function) {
        try {
            return Optional.ofNullable(function.get());
        } catch (Exception e) {
            
            if (e.getClass().equals(retryOn)) {
                return retry(function);    
            } else {
                LOGGER.error("Unknow error:", e);
                return Optional.empty();
            }
            
        }
    }
    
    private Optional<T> retry(Supplier<T> function) throws RuntimeException {
        sleep();
        LOGGER.warn("FAILED - Command failed, will be retried {} times.", maxRetries);
        
        int retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                return Optional.ofNullable(function.get());
            } catch (Exception e) {
                
                if (e.getClass().equals(retryOn)) {

                    retryCounter++;
                    LOGGER.warn("FAILED - Command failed on retry {} of {} error: {}", retryCounter, maxRetries, e);
                    
                    if (retryCounter >= maxRetries) {
                        LOGGER.error("Max retries exceeded.");
                        break;
                    } else {
                        sleep();    
                    }
                    
                } else {
                    LOGGER.error("Unknow error:", e);
                    break;
                }
                
            }
        }
        
        throw new RuntimeException("Command failed on all of " + maxRetries + " retries");        
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e1) {}
    }
}
