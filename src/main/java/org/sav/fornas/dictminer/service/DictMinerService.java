package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.springframework.stereotype.Service;

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


	private final List<TranslatorStrategy> translators;

	public void process(WordStates state) {
		DictWord word = dictWordRepository.findWordToProcess(state.getId());
		translators.stream()
				.filter(t -> t.getState() == state)
				.findFirst()
				.ifPresent(t -> {
					if (word != null) {
						((AbstractTranslator)t).execute(word, dictWordRepository);
					} else {
						log.debug("No word to process for state {}", state);
					}
				});
	}




//	public void importWordsFromFile() throws IOException {
//		try (var reader = new BufferedReader(
//				new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/words.txt"))))) {
//
//			reader.lines()
//					.map(String::trim)
//					.filter(s -> !s.isEmpty())
//					.forEach(text -> {
//						dictWordRepository.findByText(text).orElseGet(() -> {
//							DictWord w = new DictWord();
//							w.setText(text);
//							return dictWordRepository.save(w);
//						});
//					});
//		}
//	}

}



/*
!!!!definitions!!!!
https://api.dictionaryapi.dev/api/v2/entries/en/Radiates
https://github.com/meetDeveloper/freeDictionaryAPI/tree/master


https://developer.wordnik.com/docs#!/word/getWordFrequency


!!!!Translations!!!!

https://translate.googleapis.com/translate_a/t?anno=3&client=te&format=html&v=1.0&key&sl=en&tl=uk&sp=nmt&tc=1&tk=104247.461977&mode=1
anno=3&client=te&format=html&v=1.0&key&sl=en&tl=uk&sp=nmt&tc=1&tk=104247.461977&mode=1
q=contemplate

https://translator-api.glosbe.com/translateByLangWithScore?sourceLang=en&targetLang=uk
sourceLang=en&targetLang=uk
contemplate

*/