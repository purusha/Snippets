
Executor executor = Executors.newFixedThreadPool(4);
CompletionService<SomeResult> completionService = 
       new ExecutorCompletionService<SomeResult>(executor);

//4 tasks
for(int i = 0; i < 4; i++) {
   completionService.submit(new Callable<SomeResult>() {
       public SomeResult call() {
           ...
           return result;
       }
   });
}

int received = 0;
boolean erros = false;

while(received < 4 && !errors) {
      Future<SomeResult> resultFuture = completionService.take(); //blocks if none available
      try {
         SomeResult result = resultFuture.get();
         received ++;
         ... // do something with the result
      }
      catch(Exception e) {
             //log
         errors = true;
      }
}
