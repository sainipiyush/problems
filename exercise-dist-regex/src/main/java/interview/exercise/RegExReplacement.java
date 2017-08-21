package main.java.interview.exercise;

import java.util.regex.Pattern;

public class RegExReplacement {
	
	private Pattern pattern;
	private String regex;
	private String replacement = "";
	  
	RegExReplacement(String regex, String replacement) {
		this.regex = regex;
	    this.pattern = Pattern.compile(regex);
	    this.replacement = replacement;
	}
	
	String replace(String in) { 
		return pattern.matcher(in).replaceAll(replacement); 
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}	  

}
