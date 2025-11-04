package org.sav.fornas.dictminer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sav.fornas.dictminer.service.DictMinerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class DictMinerApplication implements CommandLineRunner {

	private final DictMinerService service;

	public static void main(String[] args) {
		SpringApplication.run(DictMinerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("-----job started-------");
//		service.importWordsFromFile2();
		log.info("-----job finished-------");
	}
}
