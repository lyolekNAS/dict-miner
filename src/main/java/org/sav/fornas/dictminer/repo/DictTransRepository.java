package org.sav.fornas.dictminer.repo;

import org.sav.fornas.dictminer.entity.DictTrans;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DictTransRepository extends JpaRepository<DictTrans, Long> {
	Optional<DictTrans> findByText(String text);
}
