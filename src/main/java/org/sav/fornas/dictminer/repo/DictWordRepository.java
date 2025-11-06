package org.sav.fornas.dictminer.repo;

import org.sav.fornas.dictminer.entity.DictWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DictWordRepository extends JpaRepository<DictWord, Long> {
	Optional<DictWord> findByText(String text);

	@Query(value = """
        SELECT *
            FROM dict_word dw
            WHERE (dw.state & :state) = 0
            ORDER BY RAND()
            LIMIT 1
        """, nativeQuery = true)
	DictWord findWordToProcess(@Param("state") Integer state);
}
