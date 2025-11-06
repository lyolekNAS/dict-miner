package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.model.azure.AzureRequest;
import org.sav.fornas.dictminer.model.azure.AzureResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AzureTranslator {

	private final RestTemplate azureRestTemplate;

	@Value("${app-props.url.azure}")
	private String baseURL;
	@Value("${trans_azure_key}")
	private String apiKey;



	public AzureResponse translate(String text) {
		AzureRequest ar = new AzureRequest();
		ar.setText(text);
		AzureResponse[] aResp = azureRestTemplate.postForObject("/dictionary/lookup?api-version=3.0&from=en&to=uk", List.of(ar), AzureResponse[].class);
		return aResp != null ? aResp[0] : new AzureResponse();
	}
}
