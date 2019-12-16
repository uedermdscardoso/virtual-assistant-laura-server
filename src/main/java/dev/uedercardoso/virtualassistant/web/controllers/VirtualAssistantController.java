package dev.uedercardoso.virtualassistant.web.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.watson.assistant.v2.model.MessageResponse;

import dev.uedercardoso.virtualassistant.config.AssistantConfig;
import dev.uedercardoso.virtualassistant.models.VirtualAssistant;
import dev.uedercardoso.virtualassistant.web.services.AssistantService;

@RestController
@RequestMapping("/assistant")
public class VirtualAssistantController {

	@Autowired
	private AssistantService assistantService;
	
	//Using Assistent V2 - Watson Assistant / IBM
	@RequestMapping(value="/conversation",method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getRobotText(
			@RequestParam(required=false) String message, @RequestParam(required=false) MultipartFile audioFile, @RequestParam(required=true) Boolean isAudio){
		try {
			
			VirtualAssistant laura = new VirtualAssistant("Laura",AssistantConfig.API_KEY, AssistantConfig.ASSISTANT_ID, message, AssistantConfig.TODAY, AssistantConfig.SERVICE_URL);
			
			//Using Watson Assistant
			MessageResponse response = this.assistantService.getRobotText(laura);
			
			String robotText = this.assistantService.getText(response.toString());	
			JSONObject result = new JSONObject();
			result.put("text", robotText);
			
			if(isAudio) {
				this.assistantService.executeAudio(robotText);
				
				return ResponseEntity.ok(result.toString());	
			} else
				return ResponseEntity.ok(result.toString());
			
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("/speech-to-text")
	public ResponseEntity<String> convertSpeechToText(){
		try {
			String teste = this.assistantService.convertSpeechToText();
			return ResponseEntity.ok(teste);
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	//https://cloud.ibm.com/apidocs/assistant/assistant-v2?code=java
	//https://cloud.ibm.com/apidocs/text-to-speech/text-to-speech?code=java
	//https://cloud.ibm.com/apidocs/speech-to-text/speech-to-text?code=java
		
}


