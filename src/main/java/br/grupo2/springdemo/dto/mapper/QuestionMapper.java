package br.grupo2.springdemo.dto.mapper;

import org.mapstruct.*;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.dto.QuestionDto;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    Question toQuestion(QuestionDto questionDto);

    @Mappings({
        @Mapping(target = "author.questions", ignore = true),
        @Mapping(target = "author.answers", ignore = true),
    })
    QuestionDto toQuestionDto(Question question);

}
