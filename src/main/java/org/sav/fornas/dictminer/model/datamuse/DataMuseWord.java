package org.sav.fornas.dictminer.model.datamuse;

import lombok.Data;

import java.util.List;

@Data
public class DataMuseWord {
	private String word;
	private long score;
	private List<String> tags;
}