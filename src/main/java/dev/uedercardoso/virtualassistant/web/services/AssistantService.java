package dev.uedercardoso.virtualassistant.web.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.CreateSessionOptions;
import com.ibm.watson.assistant.v2.model.MessageInput;
import com.ibm.watson.assistant.v2.model.MessageOptions;
import com.ibm.watson.assistant.v2.model.MessageResponse;
import com.ibm.watson.assistant.v2.model.SessionResponse;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.AddAudioOptions;
import com.ibm.watson.speech_to_text.v1.model.CreateLanguageModelOptions;
import com.ibm.watson.speech_to_text.v1.model.LanguageModel;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.text_to_speech.v1.util.WaveUtils;

import dev.uedercardoso.virtualassistant.models.VirtualAssistant;

@Service
public class AssistantService {

	//Using Assistant v2
	public MessageResponse getRobotText(VirtualAssistant virtualAssistant) {
		
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
	
	public void executeAudio(String message) {
		
		String apiKey = "lJPNgGUkjAThvUh8yUaSggD5s6wkarXK_aUkkacYKUut";
		String serviceUrl = "https://stream.watsonplatform.net/text-to-speech/api";
		
		IamAuthenticator authenticator = new IamAuthenticator(apiKey);
		TextToSpeech textToSpeech = new TextToSpeech(authenticator);
		textToSpeech.setServiceUrl(serviceUrl);

		try {
		  SynthesizeOptions synthesizeOptions =
		    new SynthesizeOptions.Builder()
		      .text(message)
		      .accept("audio/wav")
		      .voice("pt-BR_IsabelaVoice")
		      .build();

		  InputStream inputStream = textToSpeech.synthesize(synthesizeOptions).execute().getResult();
		  InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

//		  File audio = File.createTempFile("allison", ".wav");
//		  audio.deleteOnExit();
//		  FileOutputStream out = new FileOutputStream(audio);
//		  IOUtils.copy(in, out);

		  //Execute audio
		  AudioInputStream stream;
		  AudioFormat format;
		  DataLine.Info info;
		  Clip clip;

		  stream = AudioSystem.getAudioInputStream(in); //Or File ou InputStream
		  format = stream.getFormat();
		  info = new DataLine.Info(Clip.class, format);
		  clip = (Clip) AudioSystem.getLine(info);
		  clip.open(stream);
		  clip.start();
		  
		  in.close();
		  inputStream.close();
		 
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
		  e.printStackTrace();
		}
	}
	
	public String getText(String response) throws Exception {
		
		JSONObject output = new JSONObject(response);
		
		if(output.has("output")) {
			output = output.getJSONObject("output");
			if(output.has("generic")) {
				JSONArray generic = output.getJSONArray("generic"); 
				
				if(!generic.isEmpty()) {
					JSONObject text = generic.getJSONObject(0);
					if(text.has("text")) {
						
						return text.getString("text");
						
					}
				}
			}
		}
		
		throw new Exception("Não foi possível retornar o texto");
		
	}
	
	
	public String convertSpeechToText() {
		
		String apiKey = "LmuHvOjQHLt1DYkI9KJToMYlExQphiZmDk9khwJucdJW";
		String serviceUrl = "https://stream.watsonplatform.net/speech-to-text/api";
		
		IamAuthenticator authenticator = new IamAuthenticator(apiKey);
		SpeechToText speechToText = new SpeechToText(authenticator);
		speechToText.setServiceUrl(serviceUrl);

		CreateLanguageModelOptions createLanguageModelOptions =
		  new CreateLanguageModelOptions.Builder()
		    .name("First example language model")
		    .baseModelName("en-US_BroadbandModel")
		    .description("First custom language model example")
		    .build();

		LanguageModel languageModel =
		  speechToText.createLanguageModel(createLanguageModelOptions).execute().getResult();
		
		String customizationId = languageModel.getCustomizationId();
		
		try {
		  AddAudioOptions addAudioOptions = new AddAudioOptions.Builder()
		    .customizationId(customizationId)
		    .contentType("audio/wav")
		    .audioResource(new File("C:/Users/User/Desktop/hello_world.wav"))
		    .audioName("hello_world")
		    .build();

		  return speechToText.addAudio(addAudioOptions).execute().getResult().toString();
		  
		  // Poll for audio status.
		} catch (FileNotFoundException e) {
		  e.printStackTrace();
		}
		
		return "";
		
	}
	
}
