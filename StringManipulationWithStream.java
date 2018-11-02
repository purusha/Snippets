    public static void main(String[] args) {
        
        final List<String> toRemove = Arrays.asList("1", "2", "3");
        final String source = "Hello 1 2 3 Cicciooooo";
        
        final String result = toRemove.stream()
            .map(toRem-> (Function<String,String>)s -> s.replaceAll(toRem, ""))
            .reduce(Function.identity(), Function::andThen)
            .apply(source);
        
        System.out.println("source: " + source);
        System.out.println("result: " + result);
        
    }
