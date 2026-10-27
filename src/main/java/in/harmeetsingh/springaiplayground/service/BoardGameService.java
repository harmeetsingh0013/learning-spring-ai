package in.harmeetsingh.springaiplayground.service;

import in.harmeetsingh.springaiplayground.models.Answer;
import in.harmeetsingh.springaiplayground.models.Question;

public interface BoardGameService {
    Answer askQuestion(Question question);
}
