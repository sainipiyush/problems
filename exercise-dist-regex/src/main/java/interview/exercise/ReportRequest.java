package main.java.interview.exercise;

public class ReportRequest extends AbstractFileData {

	private String filePath;
	private String matchingWord;
	
	ReportRequest(String filePath, String matchingWord) {
		this.filePath = filePath;
		this.matchingWord = matchingWord;		
	}
	
	public String getFileName() {
		return filePath;
	}
	public void setFileName(String fileName) {
		this.filePath = fileName;
	}
	
	public String getMatchingWord() {
		return matchingWord;
	}
	public void setMatchingWord(String matchingWord) {
		this.matchingWord = matchingWord;
	}
	
	
	
}
