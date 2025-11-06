package org.sav.fornas.dictminer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictTrans;
import org.sav.fornas.dictminer.entity.DictTransAzure;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.azure.AzureResponse;
import org.sav.fornas.dictminer.model.azure.AzureTranslation;
import org.sav.fornas.dictminer.model.datamuse.DatamuseWord;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictTransAzureRepository;
import org.sav.fornas.dictminer.repo.DictTransRepository;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private final DictTransAzureRepository dictTransAzureRepository;
	private final MyMemoryTranslator myMemoryTranslator;
	private final AzureTranslator azureTranslator;
	private final DatamuseService datamuseService;

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
		log.debug(">>> word:{}", word.getText());
		MyMemoryResponse resp = myMemoryTranslator.translate(word.getText());
		List<String> rez = resp.getMatches().stream()
				.filter(w -> w.getSegment().equalsIgnoreCase(word.getText()))
				.map(w -> w.getTranslation().toLowerCase())
				.filter(translation -> !translation.equals(word.getText()))
				.filter(translation -> !translation.contains(" "))
				.distinct()
				.toList();

		rez.forEach(w -> {
			DictTrans dt = DictTrans.builder()
					.dictWordId(word.getId())
					.text(w)
					.build();
			dictTransRepository.save(dt);
		});
		word.addState(WordStates.MY_MEMORY.getId());
		dictWordRepository.save(word);
		log.trace(">>> resp:{}", resp);
		log.debug(">>> rez:{}", rez);
	}

	@Transactional
	public void processWordTransAzure(DictWord word){
		log.debug(">>> word:{}", word.getText());



		AzureResponse ar = azureTranslator.translate(word.getText());

		log.debug(">>> ar:{}", ar.getTranslations().size());
		for (AzureTranslation at : ar.getTranslations()) {
			DictTransAzure dta = DictTransAzure.builder()
					.dictWordId(word.getId())
					.posTag(at.getPosTag())
					.confidence(at.getConfidence())
					.text(at.getDisplayTarget())
					.build();

			log.debug(">>> adding:{}", dta.getText());
			dictTransAzureRepository.save(dta);
		}
		word.addState(WordStates.TRANS_AZURE.getId());
		dictWordRepository.save(word);
	}

	@Transactional
	public void processWordDatamuseMeansLike(DictWord word){
		log.debug(">>> word:{}", word.getText());
		List<DatamuseWord> words = datamuseService.getMeansLike(word.getText());

		log.debug(">>> found:{}", words.size());
		words.stream()
			.filter(dw -> !dw.getWord().contains(" "))
			.forEach(dw -> {
				dictWordRepository.findByText(dw.getWord()).orElseGet(() -> {
					DictWord w = new DictWord();
					w.setText(dw.getWord());
					w.addState(WordStates.DATAMUSE_FOUND.getId());
					log.debug(">>> added:{}", w.getText());
					return dictWordRepository.save(w);
				});
		});
		word.addState(word.getState() | WordStates.DATAMUSE_ML.getId());
		dictWordRepository.save(word);
	}

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