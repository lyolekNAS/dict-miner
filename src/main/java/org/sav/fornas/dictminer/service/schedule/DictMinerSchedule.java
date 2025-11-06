package org.sav.fornas.dictminer.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.entity.DictWord;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.repo.DictWordRepository;
import org.sav.fornas.dictminer.service.DictMinerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

	@Scheduled(fixedRate = 20_000)
	public void mineTransAzure() {
		log.info(">>> started TransAzure");
		DictWord word = dictWordRepository.findWordToProcess(WordStates.TRANS_AZURE.getId());
		dictMinerService.processWordTransAzure(word);
		log.info(">>> finished TransAzure");
	}
}
