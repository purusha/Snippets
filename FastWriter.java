package it.at.skillbill;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FastWriter {

	private static final int ITERATIONS = 1;
	private static final double MEG = (Math.pow(1024, 2));
	private static final int RECORD_COUNT = 4_000_000;
	private static final String RECORD = "Help I am trapped in a fortune cookie factory\n";
	private static final int RECSIZE = RECORD.getBytes().length;
	private static final int SIZE = RECSIZE * RECORD_COUNT;

	public static void main(String[] args) throws Exception {
	    final List<String> records = new ArrayList<String>(RECORD_COUNT);	    
	    
	    for (int i = 0; i < RECORD_COUNT; i++) {
	        records.add(RECORD);
	    }
	    
	    System.out.println(records.size() + " 'records'");
	    System.out.println(SIZE / MEG + " MB");

	    for (int i = 0; i < ITERATIONS; i++) {
	        System.out.println("\nIteration " + i);

	        writeRaw(records);
	        writeBuffered(records, 8192);
	        writeBuffered(records, (int) MEG);
	        writeBuffered(records, 4 * (int) MEG);
	        memoryMappedFiles(records);
	    }
	}
	
	private static void writeRaw(List<String> records) throws IOException {
	    final File file = File.createTempFile("foo", ".txt");
	    
	    try {
	        System.out.print("Writing raw... ");
	        write(records, new FileWriter(file));
	    } finally {
	        file.delete();
	    }
	}

	private static void writeBuffered(List<String> records, int bufSize) throws IOException {
	    final File file = File.createTempFile("foo", ".txt");
	    
	    try {
	        System.out.print("Writing buffered (buffer size: " + bufSize + ")... ");
	        write(records, new BufferedWriter(new FileWriter(file), bufSize));
	    } finally {
	        file.delete();
	    }
	}

	private static void write(List<String> records, Writer writer) throws IOException {
	    long start = System.currentTimeMillis();
	    
	    for (String record: records) {	    
	        writer.write(record);	        
	    }
	    
	    writer.flush();
	    writer.close();
	    
	    long end = System.currentTimeMillis();
	    System.out.println((end - start) / 1000f + " seconds");
	}
	
	private static void memoryMappedFiles(List<String> records) throws IOException {
		final File file = File.createTempFile("foo", ".txt");		
		long start = System.currentTimeMillis();
		System.out.print("Writing memory mapped... ");
		
		try(FileChannel rwChannel = new RandomAccessFile(file, "rw").getChannel()) {
			final ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, SIZE);
			
		    for (String record: records) {	    
		    	wrBuf.put(record.getBytes());	        
		    }
		} catch (Exception e) {
		}
		
        file.delete();
	    long end = System.currentTimeMillis();
	    System.out.println((end - start) / 1000f + " seconds");		
	}
	
}
