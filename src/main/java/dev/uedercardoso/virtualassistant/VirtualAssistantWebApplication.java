package dev.uedercardoso.virtualassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VirtualAssistantWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(VirtualAssistantWebApplication.class, args);
	}
	
}
