package br.grupo2.springdemo.controller.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import br.grupo2.springdemo.domain.Question;
import br.grupo2.springdemo.domain.Tag;
import br.grupo2.springdemo.service.QuestionService;
import br.grupo2.springdemo.service.TagService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {

    private final QuestionService questionService;
    private final TagService tagService;

    @ModelAttribute("module")
    public String module() {
        return "index";
    }

    @GetMapping
    public String index(Model model) {
        List<Question> questions = questionService.findAll();

        int tagsCount = 20;
        List<Tag> tags = tagService
                .findAll()
                .stream()
                .limit(tagsCount)
                .collect(Collectors.toList());

        model.addAttribute("tags", tags);
        model.addAttribute("questions", questions);

        return "index";
    }

}
