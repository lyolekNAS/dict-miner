package org.sav.fornas.dictminer.model.mymemory;

import lombok.Getter;

@Getter
public enum WordStates {
	MY_MEMORY		((int) Math.pow(2,  0)),
	DATAMUSE_ML		((int) Math.pow(2,  1)),
	DATAMUSE_FOUND	((int) Math.pow(2,  2));

	private final int id;

	WordStates(int id){
		this.id = id;
	}
}
