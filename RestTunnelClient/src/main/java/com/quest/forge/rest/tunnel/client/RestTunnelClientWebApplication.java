package com.quest.forge.rest.tunnel.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class RestTunnelClientWebApplication extends SpringBootServletInitializer  {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestTunnelClientWebApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(RestTunnelClientWebApplication.class, args);
	}
}
