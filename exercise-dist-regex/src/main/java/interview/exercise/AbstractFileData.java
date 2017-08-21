package main.java.interview.exercise;

public abstract class AbstractFileData {
	
	private static String regex;
	private static String replacement;
	
	public static String getRegex() {
		return regex;
	}
	public static void setRegex(String regex) {
		AbstractFileData.regex = regex;
	}
	public static String getReplacement() {
		return replacement;
	}
	public static void setReplacement(String replacement) {
		AbstractFileData.replacement = replacement;
	}
	
	
	

}
