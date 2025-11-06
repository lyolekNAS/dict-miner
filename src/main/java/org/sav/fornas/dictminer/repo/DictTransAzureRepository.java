package org.sav.fornas.dictminer.repo;

import org.sav.fornas.dictminer.entity.DictTransAzure;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DictTransAzureRepository extends JpaRepository<DictTransAzure, Long> {
	Optional<DictTransAzure> findByText(String text);
}
