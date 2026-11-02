package in.harmeetsingh.springaiplayground._03_rag.controller;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Answer;
import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import in.harmeetsingh.springaiplayground._03_rag.service.BoardGameService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class RagSearchController {

    private final BoardGameService boardGameService;

    public RagSearchController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path="/ask-rag", produces = MediaType.APPLICATION_JSON_VALUE)
    public Answer ask(@RequestBody Question question) {
        return boardGameService.askQuestion(question);
    }
}
