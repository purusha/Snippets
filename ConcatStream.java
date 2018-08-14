package a;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConcatStream {

    public static void main(String[] args) {

        final List<String> data = new ArrayList<>();
        
        data.add("alal");
        data.add("ciccioaaa");
        data.add("dp");
        
        final List<String> collect = Stream.concat(
            data.stream()
                .map(e -> "/" + e),
            data.stream()
                .map(e -> "%" + e)                              
        )
            .peek(System.out::println)
            .collect(
                Collectors.toList()
            );
        
        System.out.println(collect);
        
    }

}
