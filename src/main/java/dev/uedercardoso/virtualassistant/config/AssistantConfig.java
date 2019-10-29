package dev.uedercardoso.virtualassistant.config;

import java.util.Calendar;

import org.apache.commons.lang3.time.DateFormatUtils;

public interface AssistantConfig {
	
	public static String API_KEY = "MgYKv3Tj2sfVx5kqGrOYmdiihWzoEZSA0y3FdZfXv3jv";
	public static String ASSISTANT_ID = "8d368f5a-0ac0-478a-906f-4b7c1a43994d";
	public static String SERVICE_URL = "https://gateway.watsonplatform.net/assistant/api";
	public static String TODAY = DateFormatUtils.format(Calendar.getInstance().getTime(),"yyyy-MM-dd");
	
}
