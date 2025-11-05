package org.sav.fornas.dictminer.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictTrans;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.MatchItem;
import org.sav.fornas.dictminer.model.mymemory.MyMemoryResponse;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictTransRepository;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.sav.fornas.dictminer.service.DictMinerService;
import org.sav.fornas.dictminer.service.MyMemoryTranslator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictMinerSchedule {

	private final DictWordRepository dictWordRepository;
	private final DictMinerService dictMinerService;

	@Scheduled(fixedRate = 30_000)
	public void mineMyMemory() {
		log.info(">>> started MyMemory");
		DictWord word = dictWordRepository.findWordToProcess(WordStates.MY_MEMORY.getId());
		dictMinerService.processWordMyMemory(word);
		log.info(">>> finished MyMemory");
	}

	@Scheduled(fixedRate = 5_000)
	public void mineDatamuseMeansLike() {
		log.info(">>> started DatamuseMeansLike");
		DictWord word = dictWordRepository.findWordToProcess(WordStates.DATAMUSE_ML.getId());
		dictMinerService.processWordDatamuseMeansLike(word);
		log.info(">>> finished DatamuseMeansLike");
	}
}
