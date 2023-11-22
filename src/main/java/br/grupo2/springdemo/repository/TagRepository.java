package br.grupo2.springdemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.domain.Tag;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    Optional<Tag> findByName(String name);

    Page<Tag> findByName(String name, Pageable pageable);

    Page<Tag> findAll(Pageable pageable);

    @Query("SELECT t FROM Tag t ORDER BY SIZE(t.questions) DESC")
    Page<Tag> findAllSortByMostPopular(Pageable pageable);

    @Query("SELECT q FROM Question q JOIN q.tags t WHERE t.id = :tagId")
    Page<Question> findQuestionsByTagId(@Param("tagId") Long tagId, Pageable pageable);
}
