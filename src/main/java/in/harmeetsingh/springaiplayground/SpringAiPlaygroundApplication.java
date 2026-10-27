package in.harmeetsingh.springaiplayground;

import in.harmeetsingh.springaiplayground.models.Answer;
import in.harmeetsingh.springaiplayground.models.Question;
import in.harmeetsingh.springaiplayground.service.BoardGameService;
import in.harmeetsingh.springaiplayground.service.SpringAIBoardGameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringAiPlaygroundApplication {
    private final BoardGameService boardGameService;

    public SpringAiPlaygroundApplication(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path="/ask", produces = "application/json")
    public Answer ask(@RequestBody Question question) {
        return boardGameService.askQuestion(question);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringAiPlaygroundApplication.class, args);
    }

}
