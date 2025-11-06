package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.datamuse.DataMuseWord;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataMuseService extends AbstractTranslator {

	private final RestTemplate restTemplate;
	private final DictWordRepository dictWordRepository;

	@Value("${app-props.url.datamuse}")
	private String baseURL;

	@Override
	public WordStates getState() {
		return WordStates.DATAMUSE_ML;
	}

	@Override
	public List<String> processWord(DictWord word){
		List<DataMuseWord> words = getMeansLike(word.getText());

		List<String> mined = new ArrayList<>();
		words.stream()
				.filter(dw -> !dw.getWord().contains(" "))
				.filter(dw -> !dw.getWord().contains("-"))
				.filter(dw -> !dw.getWord().contains("."))
				.filter(dw -> !dw.getWord().contains(","))
				.forEach(dw -> {
					dictWordRepository.findByText(dw.getWord()).orElseGet(() -> {
						DictWord w = new DictWord();
						w.setText(dw.getWord());
						w.addState(WordStates.DATAMUSE_FOUND.getId());
						mined.add(w.getText());
						return dictWordRepository.save(w);
					});
				});

		return mined;
	}

	private List<DataMuseWord> getMeansLike(String text) {
		return get(text, "ml");
	}

	private List<DataMuseWord> get(String text, String paramName) {
		String url = UriComponentsBuilder.fromHttpUrl(baseURL)
				.queryParam(paramName, text)
				.toUriString();

		ResponseEntity<List<DataMuseWord>> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<>() {}
		);

		return response.getBody();
	}
}
