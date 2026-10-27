package in.harmeetsingh.springaiplayground._01_basics.exceptions;

public class AnswerNotRelevantException extends RuntimeException {
    public AnswerNotRelevantException(String question, String answer) {
        super("The answer "+ answer + " is not relevant to the question: "+question);
    }
}
