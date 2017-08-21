package main.java.interview.exercise;

import java.util.concurrent.BlockingQueue;

public class FileProcessWorker implements Runnable {

	protected BlockingQueue<String> queue = null;
	private IFileProcessor fileProcessor = null;
	
	public FileProcessWorker(BlockingQueue<String> queue, IFileProcessor fileProcessor) {
        this.queue = queue;
        this.fileProcessor = fileProcessor;
    }
	
	@Override
	public void run() {
		while(true){
            try {
            	String filePath = queue.take();
            	
            	if(filePath==RegexTextReplacementInFiles.DONE) {
 //           		System.out.println("All done...");
            		break;
            	}
            	
 //           	System.out.println("Processing: "+ filePath);
                fileProcessor.processFile(filePath);                
            } catch (InterruptedException ex) {
            	System.err.println("Task interrupted");
            }
        }
	}
	
}
