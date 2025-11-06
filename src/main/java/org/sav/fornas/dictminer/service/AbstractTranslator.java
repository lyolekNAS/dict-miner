package org.sav.fornas.dictminer.service;

import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public abstract class AbstractTranslator implements TranslatorStrategy {

	@Transactional
	public void execute(DictWord word, DictWordRepository repo) {
		log.debug(">>> [{}] processing word: {}", getState(), word.getText());
		List<String> mined = processWord(word);
		log.debug(">>> [{}] mined: {}", getState(), mined);
		word.addState(getState().getId());
		repo.save(word);
	}
}
