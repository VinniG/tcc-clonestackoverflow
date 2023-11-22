package br.grupo2.springdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {

    List<Tag> findAll();

    Page<Tag> findAll(Pageable pageable);

    Optional<Tag> findById(Long id);

    Optional<Tag> findByName(String name);

    Page<Tag> findAllByMostPopular(Pageable pageable);

    Page<Question> findQuestionsByTagId(Long tagId, Pageable pageable);

    Tag save(Tag tag);

    void deleteById(Long id);

}
