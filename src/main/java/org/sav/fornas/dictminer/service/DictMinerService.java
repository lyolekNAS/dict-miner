package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictTrans;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictTransRepository;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictMinerService {

	private final DictWordRepository dictWordRepository;
	private final DictTransRepository dictTransRepository;
	private final MyMemoryTranslator myMemoryTranslator;


	public void importWordsFromFile() throws IOException {
		try (var reader = new BufferedReader(
				new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/words.txt"))))) {

			reader.lines()
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.forEach(text -> {
						dictWordRepository.findByText(text).orElseGet(() -> {
							DictWord w = new DictWord();
							w.setText(text);
							return dictWordRepository.save(w);
						});
					});
		}
	}




	@Transactional
	public void processWordMyMemory(DictWord word){
		log.info(">>> word:{}", word.getText());
		MyMemoryResponse resp = myMemoryTranslator.translate(word.getText());
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
		word.setState(word.getState() | WordStates.MY_MEMORY.getId());
		dictWordRepository.save(word);
		log.info(">>> resp:{}", resp);
		log.info(">>> rez:{}", rez);

	}
}



/*
definitions
https://api.dictionaryapi.dev/api/v2/entries/en/Radiates
https://github.com/meetDeveloper/freeDictionaryAPI/tree/master


https://developer.wordnik.com/docs#!/word/getWordFrequency


*/