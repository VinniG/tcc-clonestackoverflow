package br.grupo2.springdemo.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.service.impl.question.QuestionSortType;

public interface QuestionSortService {

    Page<Question> sort(Pageable pageable);

    boolean isSuitableFor(QuestionSortType sortType);

}
