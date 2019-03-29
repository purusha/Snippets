package com.contactlab.cplan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;
import com.google.common.collect.Lists;

public class DoSomeStuff {

    private static final Path R = Paths.get("/home/developer/Desktop/source.log");
    private static final Path W = Paths.get("/home/developer/Desktop/destination.log");
    
    public static void main(String[] args) throws Exception {
        Files.createFile(W);
        final List<String> buffer = Lists.newArrayList();
        
        try (Stream<String> stream = Files.lines(R)) {            
            
            stream.forEachOrdered(line -> {
                buffer.add(transform(line));
                
                if (buffer.size() > 5000) {
                    write(buffer);
                }                
            });
        }
        
        write(buffer);
    }

    private static String transform(String line) {
        
        /*
		real mapping ...
	*/
        
        return line;
    }

    private static void write(final List<String> buffer) {
        try {
            Files.write(W, buffer, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        buffer.clear();
    }

}
