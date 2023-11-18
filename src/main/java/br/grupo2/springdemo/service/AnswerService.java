package br.grupo2.springdemo.service;

import br.grupo2.springdemo.domain.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerService {

    List<Answer> findAll();

    Optional<Answer> findById(Long id);

    Answer save(Answer answer);

    void deleteById(Long id);

}
