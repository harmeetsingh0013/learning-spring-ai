package in.harmeetsingh.springaiplayground._02_Prompt.controller;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Answer;
import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import in.harmeetsingh.springaiplayground._02_Prompt.service.BoardGameService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class PromptController {

    private final BoardGameService boardGameService;

    public PromptController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path = "/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public Answer ask(@RequestBody @Valid Question question) {
        return boardGameService.askQuestion(question);
    }
}
