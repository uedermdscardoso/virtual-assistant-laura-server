package dev.uedercardoso.virtualassistant.web.services;

import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;

import dev.uedercardoso.virtualassistant.models.VirtualAssistant;

@Service
public class AssistantService {

	//Using Assistant v2
	public MessageResponse getData(VirtualAssistant virtualAssistant) {
		
		IamAuthenticator authenticator = new IamAuthenticator(virtualAssistant.getApiKey());
		Assistant service = new Assistant(virtualAssistant.getDate(), authenticator);
		service.setServiceUrl(virtualAssistant.getServiceUrl());

		CreateSessionOptions sessionOptions = new CreateSessionOptions.Builder(virtualAssistant.getAssistantId()).build();
		SessionResponse sessionResponse = service.createSession(sessionOptions).execute().getResult();
		String sessionId = sessionResponse.getSessionId();
		
		MessageInput input = new MessageInput.Builder()
		  .messageType("text")
		  .text(virtualAssistant.getMessage())
		  .build();

		MessageOptions options = new MessageOptions.Builder(virtualAssistant.getAssistantId(), sessionId)
		  .input(input)
		  .build();

		MessageResponse response = service.message(options).execute().getResult();
		
		return response;
	}
	
}
