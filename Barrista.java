import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Barrista
{

    // number of threads used in executor
    static final int NOTHREADS = 3;

    // time of each task
    static final int HEATWATER = 1000;
    static final int GRINDBEANS = 1000;
    static final int FROTHMILK = 1000;
    static final int BREWING = 1000;
    static final int COMBINE = 1000;

    // method to simulate work (pause current thread without throwing checked exception)
    public static void pause(long t)
    {
        try
        {
            Thread.sleep(t);
        }
        catch(Exception e)
        {
            throw new Error(e.toString());
        }
    }

    // task to heat some water
    static class HeatWater implements Supplier<String>
    {
        @Override
        public String get()
        {
            System.out.println("Heating Water");
            pause(HEATWATER);
            return "hot water";
        }
    }

    // task to grind some beans
    static class GrindBeans implements Supplier<String>
    {
        @Override
        public String get()
        {
            System.out.println("Grinding Beans");
            pause(GRINDBEANS);
            return "grinded beans";
        }
    }

    // task to froth some milk
    static class FrothMilk implements Supplier<String>
    {
        @Override
        public String get()
        {
            System.out.println("Frothing some milk");
            pause(FROTHMILK);
            return "some milk";
        }
    }

    // task to brew some coffee
    static class Brew implements BiFunction<String,String, String>
    {
        @Override
        public String apply(String groundBeans, String heatedWater)
        {
            System.out.println("Brewing coffee with " + groundBeans + " and " + heatedWater);
            pause(BREWING);
            return "brewed coffee";
        }
    }

    // task to combine brewed coffee and milk
    static class Combine implements BiFunction<String,String, String>
    {
        @Override
        public String apply(String frothedMilk, String brewedCoffee)
        {
            System.out.println("Combining " + frothedMilk + " "+ brewedCoffee);
            pause(COMBINE);
            return "Final Coffee";
        }
    }

    public static void main(String[] args)
    {
        ExecutorService executor = Executors.newFixedThreadPool(NOTHREADS);

        long startTime = System.currentTimeMillis();

        try
        {
            // create all the tasks and let the executor handle the execution order
            CompletableFuture<String> frothMilk =       CompletableFuture.supplyAsync(new FrothMilk(), executor);
            CompletableFuture<String> heatWaterFuture = CompletableFuture.supplyAsync(new HeatWater(), executor);
            CompletableFuture<String> grindBeans =      CompletableFuture.supplyAsync(new GrindBeans(), executor);

            CompletableFuture<String> brew = heatWaterFuture.thenCombine(grindBeans, new Brew());
            CompletableFuture<String> coffee =          brew.thenCombine(frothMilk,  new Combine());

            // final coffee
            System.out.println("Here is the coffee:" + coffee.get());

            // analyzing times:
            System.out.println("\n\n");
            System.out.println("Actual time: \t\t\t\t" + (System.currentTimeMillis() - startTime)/1000.0);

            // compute the quickest possible time:
            long path1 = Math.max(GRINDBEANS, HEATWATER)+ BREWING + COMBINE;
            long path2 = FROTHMILK + COMBINE;
            System.out.println("Quickest time multi-threaded:\t\t" + Math.max(path1, path2)/1000.0);

            // compute the longest possible time:
            long longestTime = HEATWATER + GRINDBEANS + FROTHMILK + BREWING + COMBINE;
            System.out.println("Quickest time single-threaded thread:\t" + longestTime/1000.0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            executor.shutdown();
        }

    }
}
