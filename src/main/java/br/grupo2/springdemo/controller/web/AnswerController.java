package br.grupo2.springdemo.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.grupo2.springdemo.controller.exception.AccountNotFoundException;
import br.grupo2.springdemo.controller.exception.AnswerNotFoundException;
import br.grupo2.springdemo.controller.exception.QuestionNotFoundException;
import br.grupo2.springdemo.domain.Account;
import br.grupo2.springdemo.domain.Answer;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.service.AccountService;
import br.grupo2.springdemo.service.AnswerService;
import br.grupo2.springdemo.service.QuestionService;

import javax.validation.Valid;

import static br.grupo2.springdemo.controller.ControllerConstants.ANSWERS_PATH;
import static br.grupo2.springdemo.controller.ControllerConstants.QUESTIONS_PATH;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ANSWERS_PATH)
@AllArgsConstructor
public class AnswerController {

    private static final String TEMPLATE_DIR = "answer";
    private static final String LIST_TEMPLATE = TEMPLATE_DIR + "/list";
    private static final String EDIT_TEMPLATE = TEMPLATE_DIR + "/edit";

    @Qualifier("questionServiceImpl")
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final AccountService accountService;

    @GetMapping
    public String findAll(Model model) {
        List<Answer> answers = answerService.findAll();

        model.addAttribute("answers", answers);

        return LIST_TEMPLATE;
    }

    @PostMapping
    public String createAnswer(@Valid @ModelAttribute Answer answer,
                               Principal principal) {

        String userEmail = principal.getName();
        Account author = accountService.findByEmail(userEmail)
                .orElseThrow(() -> new AccountNotFoundException(userEmail));

        Long id = answer.getQuestion().getId();

        Question question = questionService.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        answer.setAuthor(author);
        answer.setQuestion(question);
        answerService.save(answer);

        return String.format("redirect:%s/%d", QUESTIONS_PATH, id);
    }

    @PatchMapping(value = "/{id}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map voteUp(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        Account author = accountService.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException(userEmail));
        Answer answer = answerService.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
        answer.removeNegativeVote(author);
        answer.addPositiveVote(author);
        answerService.save(answer);
        Integer rating = answer.getRating();
        return Collections.singletonMap("rating", rating);
    }

    @PatchMapping(value = "/{id}/dislike", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map voteDown(@PathVariable Long id, Principal principal) {
        String userEmail = principal.getName();
        Account author = accountService.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException(userEmail));
        Answer answer = answerService.findById(id).orElseThrow(() -> new AnswerNotFoundException(id));
        answer.removePositiveVote(author);
        answer.addNegativeVote(author);
        answerService.save(answer);
        Integer rating = answer.getRating();
        return Collections.singletonMap("rating", rating);
    }

    @GetMapping("/edit/{id}")
    public String getEditAnswerForm(@PathVariable Long id, Model model, Principal principal) {
        Answer answer = answerService.findById(id)
                .orElseThrow(() -> new AnswerNotFoundException(id));

        // Verificar se o usuário autenticado é o autor da resposta
        if (!answer.getAuthor().getEmail().equals(principal.getName())) {
            // Redirecionar para a visualização da resposta se não for o autor
            return "redirect:/questions/" + answer.getQuestion().getId();
        }

        model.addAttribute("answer", answer);
        return EDIT_TEMPLATE;
    }

    @PutMapping
    public String editAnswer(@Valid @ModelAttribute Answer answer, Principal principal) {
        // Verificar se o usuário autenticado é o autor da resposta
        if (!answer.getAuthor().getEmail().equals(principal.getName())) {
            // Redirecionar para a visualização da resposta se não for o autor
            return "redirect:/questions/" + answer.getQuestion().getId();
        }

        answerService.save(answer);

        return String.format("redirect:%s/%d", QUESTIONS_PATH, answer.getQuestion().getId());
    }
}
