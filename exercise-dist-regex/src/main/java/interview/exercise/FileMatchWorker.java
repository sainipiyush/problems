package main.java.interview.exercise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMatchWorker implements Runnable {

	private String filePath = null;
    private String regex = null;
    private final static int BUFFER_SIZE = 2 * 1024;
    protected BlockingQueue<String> queue = null;

    FileMatchWorker(BlockingQueue<String> queue, String filePath, String regex) {
        this.filePath = filePath;
        this.regex = regex;
        this.queue = queue;
    }
    
    @Override
    public void run() {
    	FileInputStream fileInputStream = null;
    	FileChannel ch = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            ch = fileInputStream.getChannel();
            MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY,
                    0L, ch.size());
            byte[] barray = new byte[BUFFER_SIZE];
            long checkSum = 0L;
            int nGet;
            while (mb.hasRemaining()) {
                nGet = Math.min(mb.remaining(), BUFFER_SIZE);
                ByteBuffer buff = mb.get(barray, 0, nGet);
                for (int i = 0; i < nGet; i++) {
                	checkSum += barray[i];
                }
                   
                String input = new String(barray);
                
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input);                
                if(matcher.find()) {
                	queue.put(filePath);
                	FileReportGenerator.fileCounter.incrementAndGet();
   //             	System.out.println("Matched: "+ filePath);
                	break;
                }
                //System.out.println(new String(barray));
            }
        } catch (FileNotFoundException fnfex) {
        	System.err.println("Error occured: " + fnfex.getMessage());
        } catch (Exception ex) {
        	System.err.println("Error occured: " + ex.getMessage());
        } finally {
        	try {
				ch.close();
				fileInputStream.close();
			} catch (IOException e) {
				System.err.println("Error occured while closing connections : " + e.getMessage());
			}
        }
        
//        System.out.println("exiting..FileMatchWorker");
        
    } 
	
}
