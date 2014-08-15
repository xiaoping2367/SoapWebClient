package com.minxia.model;

public class SoapForm {
	private String url;
	private String action;
	private boolean useSSL;
	private String properties;
	private String input;
	private String output;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isUseSSL() {
		return useSSL;
	}
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	
	@Override
	public String toString() {
		return "SoapForm [url=" + url + ", action=" + action + ", useSSL=" + useSSL
				+ ", properties=" + properties
				+ ", input=" + input + "]";
	}
	

}
