package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictTrans;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictTransRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyMemoryTranslator extends AbstractTranslator {

	private final RestTemplate restTemplate;
	private final DictTransRepository dictTransRepository;

	@Value("${app-props.url.my-memory}")
	private String baseURL;
	@Value("${app-props.email}")
	private String email;



	@Override
	public WordStates getState() {
		return WordStates.MY_MEMORY;
	}


	@Override
	public List<String> processWord(DictWord word){
		MyMemoryResponse resp = translate(word.getText());

		List<String> rez = resp.getMatches().stream()
				.filter(w -> w.getSegment().equalsIgnoreCase(word.getText()))
				.map(w -> w.getTranslation().toLowerCase())
				.filter(translation -> !translation.equals(word.getText()))
				.distinct()
				.toList();

		rez.forEach(w -> {
			DictTrans dt = DictTrans.builder()
					.dictWordId(word.getId())
					.text(w)
					.build();
			dictTransRepository.save(dt);
		});
		return rez;
	}

	private MyMemoryResponse translate(String text) {
		String url = UriComponentsBuilder.fromHttpUrl(baseURL)
				.queryParam("q", text)
				.queryParam("langpair",  "en|uk")
				.queryParam("de", email)
				.toUriString()
				.replace("%7C", "|");

		return restTemplate.getForObject(url, MyMemoryResponse.class);
	}
}
