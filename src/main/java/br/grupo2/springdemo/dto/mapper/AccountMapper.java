package br.grupo2.springdemo.dto.mapper;

import java.beans.JavaBean;

import org.mapstruct.*;
import org.springframework.context.annotation.ComponentScan;

import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.dto.AccountDto;
import br.grupo2.springdemo.dto.AccountPostDto;
import br.grupo2.springdemo.dto.QuestionDto;

@JavaBean
@ComponentScan
@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "questions", qualifiedByName = "questionsToQuestionDto")
    AccountDto toAccountDto(Account account);

    @Named("questionsToQuestionsDto")
    @Mapping(target = "author", expression = "java(null)")
    QuestionDto toQuestionDto(Question question);

    @Mappings({
            @Mapping(target = "questions", ignore = true),
            @Mapping(target = "password", ignore = true)
    })
    Account toAccount(AccountDto accountDto);

    @Mapping(target = "id", ignore = true)
    Account postDtoToAccount(AccountPostDto accountPostDto);

}
