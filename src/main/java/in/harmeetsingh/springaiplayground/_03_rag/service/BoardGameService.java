package in.harmeetsingh.springaiplayground._03_rag.service;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Answer;
import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import reactor.core.publisher.Flux;

public interface BoardGameService {
    Answer askQuestion(Question question);
}
