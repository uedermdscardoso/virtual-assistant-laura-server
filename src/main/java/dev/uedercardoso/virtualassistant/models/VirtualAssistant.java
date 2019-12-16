package dev.uedercardoso.virtualassistant.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VirtualAssistant implements Serializable {
	
	private static final long serialVersionUID = -817163757205052825L;

	private String name; 
	private String apiKey;
	private String assistantId;
	private String message;
	private String date;
	private String serviceUrl;
	
	public VirtualAssistant() {
		
	}

	public VirtualAssistant(String name, String apiKey, String assistantId, String message, String date, String serviceUrl) {
		this.name = name;
		this.apiKey = apiKey;
		this.assistantId = assistantId;
		this.message = message;
		this.date = date;
		this.serviceUrl = serviceUrl;
	}
	
}
