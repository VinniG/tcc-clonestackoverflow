package br.grupo2.springdemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.grupo2.springdemo.domain.Answer;
import br.grupo2.springdemo.repository.AnswerRepository;
import br.grupo2.springdemo.service.AnswerService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

    // Retorna todas as respostas
    @Override
    @Transactional(readOnly = true)
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    // Retorna uma resposta pelo ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Answer> findById(Long id) {
        return answerRepository.findById(id);
    }

    // Salva uma resposta
    @Override
    @Transactional
    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    // Exclui uma resposta pelo ID
    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            answerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            // Log para informar que a entidade não existe
            log.info("Delete non-existing entity with id=" + id, ex);
        }
    }
}
