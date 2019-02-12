package springapp;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateBuilder {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
       //return builder.errorHandler(new /*TemplateErrorHandler()*/DefaultResponseErrorHandler()).build();
    	return builder.build();
    }
}