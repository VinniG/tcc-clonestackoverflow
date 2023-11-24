package br.grupo2.springdemo.controller.web;

import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.Formatter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.grupo2.springdemo.controller.exception.AccountNotFoundException;
import br.grupo2.springdemo.controller.exception.QuestionNotFoundException;
import br.grupo2.springdemo.controller.exception.TagNotFoundException;
import br.grupo2.springdemo.domain.*;
import br.grupo2.springdemo.service.*;
import br.grupo2.springdemo.service.impl.question.QuestionSortType;

import javax.validation.Valid;

import static br.grupo2.springdemo.controller.ControllerConstants.QUESTIONS_PATH;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(QUESTIONS_PATH)
@AllArgsConstructor
public class QuestionController {

    private static final String TEMPLATE_DIR = "question";
    private static final String NEW_TEMPLATE = TEMPLATE_DIR + "/new";
    private static final String LIST_TEMPLATE = TEMPLATE_DIR + "/list";
    private static final String EDIT_TEMPLATE = TEMPLATE_DIR + "/edit";
    private static final String VIEW_TEMPLATE = TEMPLATE_DIR + "/view";

    private final QuestionService questionService;
    private final AccountService accountService;
    private final TagService tagService;
    private final List<QuestionSortService> questionSortServices;

    // Inicializa o binder para processar os dados antes da ligação
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addCustomFormatter(new Formatter<Set<Tag>>() {
            @Override
            public Set<Tag> parse(String s, Locale locale) throws ParseException {
                Set<Tag> tagSet = new HashSet<>();
                Arrays.stream(s.split("\\s+"))
                        .forEach(tag -> tagService.findByName(tag)
                                .ifPresentOrElse(tagSet::add, () -> tagSet.add(new Tag(tag))));
                return tagSet;
            }

            @Override
            public String print(Set<Tag> tags, Locale locale) {
                return tags.stream().map(Tag::toString).collect(Collectors.joining(" "));
            }
        }, "tags");
    }

    // Adiciona um atributo ao modelo
    @ModelAttribute("module")
    public String module() {
        return "questions";
    }

    // Mapeia a solicitação GET para listar todas as perguntas
    @GetMapping
    public String findAll(Model model,
                          @RequestParam(value = "filters", required = false) String filters,
                          @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {

        if (!Objects.isNull(filters)) {
            List<String> filtersList = List.of(filters.split(","));
            // Processa os filtros, se necessário
        }

        Optional<QuestionSortType> sortType = pageable
            .getSort()
            .get()
            .map(Sort.Order::getProperty)
            .map(type -> CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, type))
            .map(QuestionSortType::valueOf)
            .findFirst();

        // Ordena as perguntas com base no tipo de ordenação especificado
        sortType.flatMap(questionSortType -> questionSortServices
                .stream()
                .filter(service -> service.isSuitableFor(questionSortType))
                .findFirst()).ifPresent(service -> model.addAttribute("questions", service.sort(pageable)));

        return LIST_TEMPLATE;
    }

    // Mapeia a solicitação GET para listar perguntas com uma determinada tag
    @GetMapping("/tagged/{tagName}")
    public String taggedQuestions(@PathVariable String tagName, Model model,
                                @PageableDefault(
                                        sort = {"id"},
                                        direction = Sort.Direction.DESC,
                                        size = 5) Pageable pageable) {
        Tag tag = tagService.findByName(tagName).orElseThrow(() -> new TagNotFoundException(tagName));

        return LIST_TEMPLATE;
    }

    // Mapeia a solicitação GET para criar uma nova pergunta
    @GetMapping("/new")
    public String askQuestion(Model model) {
        model.addAttribute("question", new Question());

        return NEW_TEMPLATE;
    }

    // Mapeia a solicitação POST para salvar uma nova pergunta
    @PostMapping
    public String saveQuestion(@Valid @ModelAttribute Question question, Principal principal, RedirectAttributes redirectAttributes) {
        // Obtém o email do usuário autenticado
        String userEmail = principal.getName();
        // Encontra a conta associada ao email
        Account author = accountService.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User with this email not found: " + userEmail));

        // Define o autor da pergunta e salva
        question.setAuthor(author);
        questionService.save(question);

        // Adiciona uma mensagem de sucesso
        redirectAttributes.addFlashAttribute("successMessage", "Pergunta criada com sucesso!");

        // Redireciona para a página de visualização da pergunta recém-criada
        return "redirect:/questions/" + question.getId();
    }

    // Mapeia a solicitação GET para visualizar uma pergunta por ID
    @GetMapping("/{id}")
    public String viewQuestionById(@PathVariable Long id, Model model, Principal principal) {
        // Obtém a pergunta por ID
        Question question = questionService
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        // Incrementa as visualizações
        question.incrementViews();
        questionService.save(question);

        // Cria uma resposta associada à pergunta
        Answer answer = new Answer();
        answer.setQuestion(question);

        // Adiciona question e answer ao modelo
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);

        // Retorna o template de visualização
        return VIEW_TEMPLATE;
    }

    // Mapeia a solicitação PATCH para votar positivamente em uma pergunta
    @PatchMapping(value = "/{id}/like", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map voteUp(@PathVariable Long id, Principal principal) {
        // Obtém o email do usuário autenticado
        String userEmail = principal.getName();
        // Encontra a conta associada ao email
        Account author = accountService.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException(userEmail));
        // Obtém a pergunta por ID
        Question question = questionService.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));

        // Remove um voto negativo (dislike) se existir
        question.removeNegativeVote(author);
        // Adiciona um voto positivo (like)
        question.addPositiveVote(author);
        // Salva as alterações
        questionService.save(question);
        // Obtém a classificação atualizada
        Integer rating = question.getRating();

        // Retorna a classificação como resposta
        return Collections.singletonMap("rating", rating);
    }

    // Mapeia a solicitação PATCH para votar negativamente em uma pergunta
    @PatchMapping(value = "/{id}/dislike", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map voteDown(@PathVariable Long id, Principal principal) {
        // Obtém o email do usuário autenticado
        String userEmail = principal.getName();
        // Encontra a conta associada ao email
        Account author = accountService.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException(userEmail));
        // Obtém a pergunta por ID
        Question question = questionService.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));

        // Remove um voto positivo (like) se existir
        question.removePositiveVote(author);
        // Adiciona um voto negativo (dislike)
        question.addNegativeVote(author);
        // Salva as alterações
        questionService.save(question);
        // Obtém a classificação atualizada
        Integer rating = question.getRating();

        // Retorna a classificação como resposta
        return Collections.singletonMap("rating", rating);
    }

    // Mapeia a solicitação GET para exibir o formulário de edição de uma pergunta
    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable Long id, Model model, Principal principal) {
        Question question = questionService.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));

        // Verificar se o usuário autenticado é o autor da pergunta
        if (!question.getAuthor().getEmail().equals(principal.getName())) {
            // Redirecionar para a visualização da pergunta se não for o autor
            return "redirect:/questions/" + id;
        }

        model.addAttribute("question", question);
        return EDIT_TEMPLATE;
    }

    @PutMapping
    public String editQuestion(@Valid @ModelAttribute Question question, Principal principal, RedirectAttributes redirectAttributes) {
        // Verificar se o usuário autenticado é o autor da pergunta
        if (!question.getAuthor().getEmail().equals(principal.getName())) {
            // Redirecionar para a visualização da pergunta se não for o autor
            return "redirect:/questions/" + question.getId();
        }

        // Verificar se a pergunta foi realmente editada antes de salvar
        Question existingQuestion = questionService.findById(question.getId())
                .orElseThrow(() -> new QuestionNotFoundException(question.getId()));

        if (!existingQuestion.equals(question)) {
            // Se a pergunta foi editada, salvar as alterações
            questionService.save(question);
            // Adicionar uma mensagem de sucesso
            redirectAttributes.addFlashAttribute("successMessage", "Pergunta editada com sucesso!");
        }

        // Redirecionar para a visualização da pergunta após a edição
        return String.format("redirect:%s/%d", QUESTIONS_PATH, question.getId());
    }

    // Mapeia a solicitação DELETE para excluir uma pergunta
    @DeleteMapping("/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        // Exclui a pergunta por ID
        questionService.deleteById(id);
        // Redireciona para a página de listagem de perguntas
        return "redirect:/questions";
    }
}
