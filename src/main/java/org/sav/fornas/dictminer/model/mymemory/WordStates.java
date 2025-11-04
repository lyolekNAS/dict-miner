package org.sav.fornas.dictminer.model.mymemory;

import lombok.Getter;

@Getter
public enum WordStates {
	MY_MEMORY(1),
	DEFENITION(2);

	private final int id;

	WordStates(int id){
		this.id = id;
	}
}
