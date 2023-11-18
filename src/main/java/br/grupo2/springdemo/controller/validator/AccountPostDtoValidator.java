package br.grupo2.springdemo.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import br.grupo2.springdemo.dto.AccountPostDto;

@Component
public class AccountPostDtoValidator implements Validator {

    // Verifica se a classe é suportada
    @Override
    public boolean supports(Class<?> aClass) {
        return AccountPostDto.class.equals(aClass);
    }

    // Valida o objeto
    @Override
    public void validate(Object o, Errors errors) {
        AccountPostDto accountPostDto = (AccountPostDto) o;

        // Verifica se a senha e a confirmação de senha são iguais
        if (!accountPostDto.getPasswordConfirm().equals(accountPostDto.getPassword())) {
            errors.rejectValue("passwordConfirm", "error.validation.password.confirmation");
        }
    }
}
