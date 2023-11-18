package br.grupo2.springdemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.grupo2.springdemo.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Page<Question> findAll(Pageable pageable);

    @Query("SELECT q FROM Question q ORDER BY q.createdDate DESC")
    Page<Question> findAllSortByNewest(Pageable pageable);

    @Query(
    value = "SELECT q FROM Question q " +
            "LEFT JOIN Answer a ON q.id = a.question.id " +
            "GROUP BY q " +
            "ORDER BY GREATEST(MAX(q.lastModifiedDate), COALESCE(MAX(a.lastModifiedDate), '0001-01-01 00:00:00')) DESC",
        countQuery = "SELECT COUNT(DISTINCT q.id) FROM Question q LEFT JOIN Answer a ON q.id = a.question.id"
    )
    Page<Question> findAllSortByLastActive(Pageable pageable);

    @Query("SELECT q FROM Question q ORDER BY SIZE(q.positiveVotes) - SIZE(q.negativeVotes) DESC")
    Page<Question> findAllSortByMostVotes(Pageable pageable);
}
