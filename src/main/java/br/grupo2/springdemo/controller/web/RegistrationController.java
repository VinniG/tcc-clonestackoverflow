package br.grupo2.springdemo.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.grupo2.springdemo.controller.validator.AccountPostDtoValidator;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.dto.AccountPostDto;
import br.grupo2.springdemo.dto.mapper.AccountMapper;
import br.grupo2.springdemo.service.AccountService;

import static br.grupo2.springdemo.controller.ControllerConstants.REGISTRATION_PATH;

import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping(REGISTRATION_PATH)
public class RegistrationController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountPostDtoValidator accountPostDtoValidator;
    private final MessageSource messageSource;

    // Adiciona um atributo ao modelo
    @ModelAttribute("module")
    String module() {
        return "registration";
    }

    // Mapeia a solicitação GET para exibir o formulário de registro
    @GetMapping
    public String registration(AccountPostDto accountPostDto) {
        return "registration";
    }

    // Mapeia a solicitação POST para processar o formulário de registro
    @PostMapping
    public String registration(@Validated @ModelAttribute("accountPostDto") AccountPostDto accountPostDto,
                               BindingResult bindingResult,
                               Model model,
                               Locale locale) {

        // Valida o formulário usando o validador personalizado
        accountPostDtoValidator.validate(accountPostDto, bindingResult);

        // Se houver erros de validação, retorna para o formulário de registro
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        // Verifica se já existe uma conta com o mesmo email
        Optional<Account> account = accountService.findByEmail(accountPostDto.getEmail());

        if (account.isPresent()) {
            // Se a conta já existir, adiciona uma mensagem de erro e retorna para o formulário de registro
            String errorMessage = messageSource.getMessage("error.registration.user.exist", null, locale);
            model.addAttribute("error", errorMessage);
            return "registration";
        }

        // Converte o DTO para uma entidade de conta e salva no serviço de conta
        accountService.save(accountMapper.postDtoToAccount(accountPostDto));

        // Redireciona para a página de login após o registro bem-sucedido
        return "redirect:login";
    }
}