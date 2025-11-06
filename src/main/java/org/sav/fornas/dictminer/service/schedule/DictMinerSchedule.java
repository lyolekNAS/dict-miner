package org.sav.fornas.dictminer.service.schedule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.model.mymemory.WordStates;
import org.sav.fornas.dictminer.service.DictMinerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DictMinerSchedule {

	private final DictMinerService dictMinerService;

	@Scheduled(fixedRate = 5_000)
	public void mineDataMuse() {
		dictMinerService.process(WordStates.DATAMUSE_ML);
	}

	@Scheduled(fixedRate = 30_000)
	public void mineMyMemory() {
		dictMinerService.process(WordStates.MY_MEMORY);
	}

	@Scheduled(fixedRate = 20_000)
	public void mineAzure() {
		dictMinerService.process(WordStates.TRANS_AZURE);
	}
}
