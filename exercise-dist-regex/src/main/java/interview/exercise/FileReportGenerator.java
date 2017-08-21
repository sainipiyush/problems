package main.java.interview.exercise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileReportGenerator implements IFileReport{

	private static FileReportGenerator fileReportGenerator = null;
	protected static AtomicInteger fileCounter = new AtomicInteger(0);
	
	protected static ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

	@Override
	public void generateReport(ReportRequest request) {
		
		increment(request.getMatchingWord());
		
	}
	
	public synchronized void increment(String word) {
		map.put(word, map.getOrDefault(word, 0) + 1);
	}
	
	public static FileReportGenerator getInstance() {
		if (fileReportGenerator == null) {
			synchronized (FileReportGenerator.class) {
				if (fileReportGenerator == null) {
					fileReportGenerator = new FileReportGenerator();
				}
			}
		}
		return fileReportGenerator;
	}

	@Override
	public void printReport() {
		System.out.println("Processed " + FileReportGenerator.fileCounter + " file(s)");
        System.out.println("Replaced to '" + AbstractFileData.getReplacement() + "' :"); 
        for (Map.Entry<String, Integer> entry : FileReportGenerator.map.entrySet()) {
        	System.out.println(entry.getKey() + " : " + entry.getValue() + " occurrence");
        }
		
	}
	
	

}
