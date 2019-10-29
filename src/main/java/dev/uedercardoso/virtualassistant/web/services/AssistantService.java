package dev.uedercardoso.virtualassistant.web.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.GetPronunciationOptions;
import com.ibm.watson.text_to_speech.v1.model.Pronunciation;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

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
	
	public Pronunciation pronnunciation() {
		String apikey = "lJPNgGUkjAThvUh8yUaSggD5s6wkarXK_aUkkacYKUut";
		String serviceUrl = "https://stream.watsonplatform.net/text-to-speech/api";
		
		IamAuthenticator authenticator = new IamAuthenticator(apikey);
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl(serviceUrl);

		GetPronunciationOptions getPronunciationOptions =
		  new GetPronunciationOptions.Builder()
		    .text("Hello, I am Alice")
		    .format("ibm")
		    .voice("en-US_AllisonVoice")
		    .build();

		Pronunciation pronunciation =
		  textToSpeech.getPronunciation(getPronunciationOptions).execute().getResult();
		
		return pronunciation;
	}
	
	public void saveAudio() {
		String apikey = "lJPNgGUkjAThvUh8yUaSggD5s6wkarXK_aUkkacYKUut";
		String serviceUrl = "https://stream.watsonplatform.net/text-to-speech/api";
		
		IamAuthenticator authenticator = new IamAuthenticator(apikey);
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl(serviceUrl);

		try {
		  SynthesizeOptions synthesizeOptions =
		    new SynthesizeOptions.Builder()
		      .text("Hello world, I am Alice")
		      .accept("audio/wav")
		      .voice("en-US_AllisonVoice")
		      .build();

		  InputStream inputStream =
		    textToSpeech.synthesize(synthesizeOptions).execute().getResult();
		  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

		  OutputStream out = new FileOutputStream("C:/Users/User/Desktop/hello_world.wav");
		  byte[] buffer = new byte[1024];
		  int length;
		  while ((length = in.read(buffer)) > 0) {
		    out.write(buffer, 0, length);
		  }

		  out.close();
		  in.close();
		  inputStream.close();
		} catch (IOException e) {
		  e.printStackTrace();
		}
	}
	
}
