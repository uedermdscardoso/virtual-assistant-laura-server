package dev.uedercardoso.virtualassistant.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import dev.uedercardoso.virtualassistant.config.AssistantConfig;
import dev.uedercardoso.virtualassistant.models.VirtualAssistant;
import dev.uedercardoso.virtualassistant.web.services.AssistantService;

@RestController
@RequestMapping("/assistant")
public class VirtualAssistantController {

	@Autowired
	private AssistantService assistantService;
	
	//https://cloud.ibm.com/apidocs/assistant/assistant-v2?code=java
	//Using Assistent V2 - Watson Assistant / IBM
	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getResponse(@RequestBody String message){
		
		try {
			
			VirtualAssistant laura = new VirtualAssistant("Laura",AssistantConfig.API_KEY, AssistantConfig.ASSISTANT_ID, message,  AssistantConfig.TODAY, AssistantConfig.SERVICE_URL);
			
			//Using Watson Assistant
			MessageResponse response = this.assistantService.getData(laura);
			
			return ResponseEntity.ok(response.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//https://cloud.ibm.com/apidocs/text-to-speech/text-to-speech?code=java
	@PostMapping("/audio")
	public ResponseEntity<Void> sendSounds(@RequestBody String message){
		try {
			
			//Using text to speech api (Watson)			
			this.assistantService.sendSounds(message);
			
			return ResponseEntity.ok().build();
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/conversation")
	public ResponseEntity<String> getTextAndSounds(
			@RequestParam(required=true) String message, 
			@RequestParam(required=true) Boolean isTalk) {
		try {
			
			VirtualAssistant laura = new VirtualAssistant("Laura",AssistantConfig.API_KEY, AssistantConfig.ASSISTANT_ID, message,  AssistantConfig.TODAY, AssistantConfig.SERVICE_URL);
			
			//Using Watson Assistant
			String response = this.assistantService.getTextAndLoadSounds(laura,isTalk);
			
			return ResponseEntity.ok(response); //Resposta do assistente
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//https://cloud.ibm.com/apidocs/speech-to-text/speech-to-text?code=java
	@PostMapping("/teste")
	public ResponseEntity<String> convertSpeechToText(){
		try {
			String teste = this.assistantService.convertSpeechToText();
			return ResponseEntity.ok(teste);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
		
}


