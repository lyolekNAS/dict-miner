package org.sav.fornas.dictminer.service;

import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.WordStates;

import java.util.List;

public interface TranslatorStrategy {
	WordStates getState();
	List<String> processWord(DictWord word);
}
