package in.harmeetsingh.springaiplayground._02_Prompt.controller;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import in.harmeetsingh.springaiplayground._02_Prompt.service.BoardGameService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@Controller
@ResponseBody
public class AskController {
    private final BoardGameService boardGameService;

    public AskController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path="/ask-stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> ask(@RequestBody Question question) {
        return boardGameService.askQuestionInStream(question);
    }
}
