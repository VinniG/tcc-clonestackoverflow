package br.grupo2.springdemo.service.impl.question;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.repository.QuestionRepository;
import br.grupo2.springdemo.service.QuestionSortService;

@Service
@AllArgsConstructor
public class LastActiveQuestionSortService implements QuestionSortService {

    private final QuestionRepository questionRepository;

    @Override
    public Page<Question> sort(Pageable pageable) {
        Pageable unsortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        return questionRepository.findAllSortByLastActive(unsortedPageable);
    }

    @Override
    public boolean isSuitableFor(QuestionSortType sortType) {
        return QuestionSortType.LAST_ACTIVE.equals(sortType);
    }

}
