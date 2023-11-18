package br.grupo2.springdemo.service.impl.question;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.grupo2.springdemo.controller.exception.QuestionNotFoundException;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.repository.QuestionRepository;
import br.grupo2.springdemo.service.QuestionService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    // Retorna todas as perguntas paginadas
    @Override
    @Transactional(readOnly = true)
    public Page<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    // Retorna todas as perguntas
    @Override
    @Transactional(readOnly = true)
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    // Retorna uma pergunta pelo ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Question> findById(Long id) {
        return questionRepository.findById(id);
    }

    // Salva uma pergunta
    @Override
    @Transactional
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    // Exclui uma pergunta pelo ID
    @Override
    @Transactional
    public void deleteById(Long id) {
        try {
            questionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            // Log para informar que a entidade não existe
            log.info("Delete non-existing entity with id=" + id, ex);
        }
    }
}
