package org.sav.fornas.dictminer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Value("${app-props.url.azure}")
	private String baseURL;
	@Value("${trans_azure_key}")
	private String apiKey;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate azureRestTemplate(RestTemplateBuilder builder) {
		return builder
				.rootUri(baseURL)
				.additionalInterceptors((request, body, execution) -> {
					request.getHeaders().add("Ocp-Apim-Subscription-Key", apiKey);
					request.getHeaders().add("Ocp-Apim-Subscription-Region", "northeurope");
					return execution.execute(request, body);
				})
				.build();
	}
}
