package main.java.interview.exercise;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RegexTextReplacementInFiles {
	
	private static final int READER_NUM_OF_THREADS = 3;
	private static final int WRITER_NUM_OF_THREADS = 5;
	protected static final String DONE = new String("All files processed");
	private static BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	
	final static ExecutorService matchService = Executors.newFixedThreadPool(READER_NUM_OF_THREADS);
	final static ExecutorService processService = Executors.newFixedThreadPool(WRITER_NUM_OF_THREADS);

	public static void process(String startingPath, String regexPattern,
           String replacement, String fileAcceptPattern) {
		
		AbstractFileData.setRegex(regexPattern);
		AbstractFileData.setReplacement(replacement);
		
		createReaderExecutor(startingPath, regexPattern, replacement, fileAcceptPattern);
		createWriterExecutor(regexPattern, replacement);	       
    }    
  
	
	protected static void createReaderExecutor(String startingPath, String regexPattern, String replacement, String fileAcceptPattern) {
		
	    long time = System.currentTimeMillis();
	    try {
	    	
	    	if(fileAcceptPattern!=null) {
	    		String filePattern = RegexConverter.wildcardToRegex(fileAcceptPattern);
	    		 Files.walk(Paths.get(startingPath)).parallel()
	                .filter(Files::isRegularFile)
	                .filter(a -> a.getFileName().toString().matches(filePattern))
	                .forEach(p -> matchService.submit(new FileMatchWorker(queue, p.toAbsolutePath().toString(), regexPattern)));
	    	} else {
	    		
	    		Files.walk(Paths.get(startingPath)).parallel()
                .filter(Files::isRegularFile)
                .forEach(p -> matchService.submit(new FileMatchWorker(queue, p.toAbsolutePath().toString(), regexPattern)));
	    	}
	      
	        matchService.shutdown();
	        try {
	        	matchService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	        } catch (InterruptedException e) {
	        	System.err.println("tasks interrupted");
	        }
	    } catch (Exception ex) {
	    	System.err.println("Error occured: " + ex.getMessage());
	    } finally {
	        if (!matchService.isTerminated()) {
	            System.err.println("Cancel non-finished tasks");
	        }
	        matchService.shutdownNow();
//	        System.out.println("Match service shutdown finished");
	        
	        try {
	        	int counter = WRITER_NUM_OF_THREADS;
	        	while(counter > 0) {
	        		queue.put(DONE);
	        		counter--;
	        	}	        		
//				System.out.println("Added DONE to queue..");
			} catch (InterruptedException e) {
				System.err.println("queue addition interrupted");
			}
	        
	    }
//	    System.out.println((System.currentTimeMillis() - time) / 1000.0);       
	}
	
	protected static void createWriterExecutor(String regexPattern, String replacement) {
		
		try {
			
			for(int i = 0; i < WRITER_NUM_OF_THREADS; i++) {
				processService.submit(new FileProcessWorker(queue, new FileProcessor(regexPattern, replacement)));
			}			
						
			processService.shutdown();
	        try {
	        	processService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
	        } catch (InterruptedException e) {
	        	System.err.println("tasks interrupted");
	        }
		} catch (Exception ex) {
			System.err.println("Error occured: " + ex.getMessage());
	    } finally {
	        if (!processService.isTerminated()) {
	            System.err.println("Cancel non-finished tasks");
	        }
	        processService.shutdownNow();
//	        System.out.println("Process service shutdown finished");
	        
	        FileReportGenerator.getInstance().printReport();
	    }
	    
	}

    public static void main(String[] args) {
        String startingDir = null, regexPattern = null, replacement = null, fileAcceptPattern = null;
        if (args.length >= 3) {
            startingDir = args[0];
            regexPattern = args[1];
            replacement = args[2];
        }
        if (args.length >= 4) {
            fileAcceptPattern = args[3];
        }
        if (startingDir != null) {
            process(startingDir, regexPattern, replacement, fileAcceptPattern);
        } else {
            System.out.println("Expected at least 3 parameters but got " + args.length);
        }
    }

}
