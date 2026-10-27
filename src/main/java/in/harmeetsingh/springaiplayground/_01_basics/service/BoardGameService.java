package in.harmeetsingh.springaiplayground._01_basics.service;

import in.harmeetsingh.springaiplayground._01_basics.models.Answer;
import in.harmeetsingh.springaiplayground._01_basics.models.Question;

public interface BoardGameService {
    Answer askQuestion(Question question);
}
