import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.apache.commons.lang3.Range;

public class UuidUniformedDistribution {
	
	/*
	 * 	add /home/<USER>/.m2/repository/org/apache/commons/commons-lang3/3.6/commons-lang3-3.6.jar
	 */
	
	//see https://stackoverflow.com/questions/4259681/compare-two-hex-strings-in-java
	final static Comparator<String> comparator = new Comparator<String>() {
		@Override
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};
	
	public static void main(String[] args) {
		
		final Map<Range<String>, AtomicInteger> data = new HashMap<>();
		
		final List<Range<String>> ranges = new ArrayList<>();
		ranges.add(Range.between("000", "333", comparator));
		ranges.add(Range.between("334", "666", comparator));
		ranges.add(Range.between("667", "999", comparator));
		ranges.add(Range.between("99a", "ccc", comparator));
		ranges.add(Range.between("ccd", "fff", comparator));
		
		//init
		ranges.forEach(r -> {
			data.put(r, new AtomicInteger(0));
		});
		
		//load
		IntStream.range(0, 10000)
			.forEach(i -> {							
				final UUID randomUUID = UUID.randomUUID();
				final String key = randomUUID.toString().substring(0, 3);
				
				final Range<String> range = ranges.stream().filter(r -> r.contains(key)).findFirst().get();
				data.get(range).incrementAndGet();
			});

		//verify
		data.entrySet().forEach(System.out::println);
		
	}

}

