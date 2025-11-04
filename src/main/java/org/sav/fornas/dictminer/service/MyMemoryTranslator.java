package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyMemoryTranslator {

	private final RestTemplate restTemplate;

	@Value("${app-props.url.my-memory}")
	private String baseURL;
	@Value("${app-props.email}")
	private String email;

	public MyMemoryResponse translate(String text) {
		String url = UriComponentsBuilder.fromHttpUrl(baseURL)
				.queryParam("q", text)
				.queryParam("langpair",  "en|uk")
				.queryParam("de", email)
				.toUriString()
				.replace("%7C", "|");

//		log.info("url:{}", url);

		return restTemplate.getForObject(url, MyMemoryResponse.class);
	}
}
