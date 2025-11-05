package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.model.datamuse.DatamuseWord;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatamuseService {

	private final RestTemplate restTemplate;

	@Value("${app-props.url.datamuse}")
	private String baseURL;

	public List<DatamuseWord> getMeansLike(String text) {
		return get(text, "ml");
	}

	private List<DatamuseWord> get(String text, String paramName) {
		String url = UriComponentsBuilder.fromHttpUrl(baseURL)
				.queryParam(paramName, text)
				.toUriString();

		ResponseEntity<List<DatamuseWord>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {}
		);

		return response.getBody();
	}
}
