package main.java.interview.exercise;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileProcessor implements IFileProcessor {
	
	private final static int BUFFER_SIZE = 2 * 1024;
	private String regex;
	private String replacement;
	
	public FileProcessor(String regex, String replacement) {
		this.regex = regex;
	    this.replacement = replacement;
	}

	@Override
	public void processFile(String filePath) {
		
		FileOutputStream output = null;
		try {
            FileInputStream f = new FileInputStream(filePath);
            
            output = new FileOutputStream(filePath+".processed", true);
           
            
            FileChannel ch = f.getChannel();
            MappedByteBuffer mb = ch.map(FileChannel.MapMode.READ_ONLY,
                    0L, ch.size());
            
            long checkSum = 0L;
            int nGet;
            while (mb.hasRemaining()) {
            	byte[] barray = new byte[BUFFER_SIZE];
                nGet = Math.min(mb.remaining(), BUFFER_SIZE);
                ByteBuffer buff = mb.get(barray, 0, nGet);
                for (int i = 0; i < nGet; i++) {
                	checkSum += barray[i];
                }
                                                               
                String input = new String(barray);
                
     //         System.out.println(input);
                
                int from = 0;
                StringBuffer sb = new StringBuffer();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input.trim());   
                while (matcher.find(from)) {   
                	
                	String group = matcher.group(0);
                	FileReportGenerator.getInstance().generateReport(new ReportRequest(filePath, group));           	
                	from = matcher.start() + group.length();
                }        
                
                String changed = matcher.replaceAll(replacement);
                output.write(changed.getBytes());
            }
            ch.close();
            f.close();
        } catch (FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	 try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
	}

	
	
}
