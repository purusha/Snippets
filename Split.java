package a;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Split {
    
    public static <T, L, R> Pair<L, R> splitStream(
        Stream<T> inputStream, 
        Predicate<T> predicate,
        Function<Stream<T>, L> trueProcessor, 
        Function<Stream<T>, R> falseProcessor
    ) {

        final Map<Boolean, List<T>> partitioned = inputStream.collect(Collectors.partitioningBy(predicate));
        final L trueResult = trueProcessor.apply(partitioned.get(Boolean.TRUE).stream());
        final R falseResult = falseProcessor.apply(partitioned.get(Boolean.FALSE).stream());

        return new ImmutablePair<L, R>(trueResult, falseResult);
    }

    public static void main(String[] args) {
        final Stream<Integer> stream = Stream.iterate(0, n -> n + 1).limit(10);

        final Pair<List<Integer>, String> results = splitStream(
            stream,
            n -> n > 5,
            s -> s.filter(n -> n % 2 == 0).collect(Collectors.toList()),
            s -> s.map(n -> n.toString()).collect(Collectors.joining("|"))
        );

        System.out.println(results);
    }    

}
