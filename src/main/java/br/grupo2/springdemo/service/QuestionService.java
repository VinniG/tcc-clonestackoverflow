package br.grupo2.springdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.grupo2.springdemo.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    // Retorna todas as perguntas
    List<Question> findAll();

    // Retorna uma página de perguntas com suporte à paginação
    Page<Question> findAll(Pageable pageable);

    // Busca uma pergunta pelo ID
    Optional<Question> findById(Long id);
    
    // Salva uma pergunta
    Question save(Question question);

    // Exclui uma pergunta pelo ID
    void deleteById(Long id);

}
