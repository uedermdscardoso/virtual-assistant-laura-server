package dev.uedercardoso.virtualassistant.web.controllers;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getResponseFromVirtualAssistant(@RequestBody String message){
		
		try {
			
			VirtualAssistant laura = new VirtualAssistant("Laura",AssistantConfig.API_KEY, AssistantConfig.ASSISTANT_ID, message,  AssistantConfig.TODAY, AssistantConfig.SERVICE_URL);
			
			MessageResponse response = this.assistantService.getData(laura);
			
			return ResponseEntity.ok(response.toString());
			
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
}


