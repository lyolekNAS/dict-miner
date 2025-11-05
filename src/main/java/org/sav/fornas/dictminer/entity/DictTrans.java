package org.sav.fornas.dictminer.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"dictWordId", "text"})
	}
)
public class DictTrans {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long dictWordId;

	private String text;
}
