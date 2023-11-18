package br.grupo2.springdemo.dto;

import lombok.*;
import br.grupo2.springdemo.audit.Auditable;
import br.grupo2.springdemo.domain.Account;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto extends Auditable<Account> {

    private Long id;

    private String content;

    private AccountDto author;

    private Boolean isAccepted;

    private Set<AccountDto> negativeVotes;

    private Set<AccountDto> positiveVotes;

    private QuestionDto question;

}
