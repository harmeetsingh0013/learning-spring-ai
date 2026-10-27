package in.harmeetsingh.springaiplayground._01_basics.service;

import in.harmeetsingh.springaiplayground._01_basics.exceptions.AnswerNotRelevantException;
import in.harmeetsingh.springaiplayground._01_basics.models.Answer;
import in.harmeetsingh.springaiplayground._01_basics.models.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.resilience.retry.MethodRetryEvent;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Service
public class SelfEvaluatingBoardGameService implements BoardGameService {

    private static final Logger log = LoggerFactory.getLogger(SelfEvaluatingBoardGameService.class);
    private static final long MAX_RETRIES = 3;

    private final ChatClient chatClient;
    private final RelevancyEvaluator evaluator;
    private final SelfEvaluatingBoardGameService self;

    public SelfEvaluatingBoardGameService(ChatClient.Builder builder, @Lazy  SelfEvaluatingBoardGameService self) {
        var chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini");

        this.chatClient = builder.defaultOptions(chatOptions).build();
        this.evaluator = new RelevancyEvaluator(builder);
        this.self = self;
    }

    @Override
    public Answer askQuestion(Question question) {
        try {
            return self.executeWithRetry(question);
        } catch (AnswerNotRelevantException ex) {
            return new Answer("Sorry, unable to finds the answer of your question");
        }
    }

    @Retryable(value = AnswerNotRelevantException.class, maxRetries = MAX_RETRIES)
    public Answer executeWithRetry(Question question) {
        log.info("Asking board-game question; retry attempt started: {}", question.question());
        var answerText = chatClient.prompt()
                .user(question.question())
                .call()
                .content();

        evaluateRelevancy(question, answerText);
        return new Answer(answerText);
    }

    @EventListener
    void logRetry(MethodRetryEvent event) {
        if (!event.getMethod().getDeclaringClass().equals(SelfEvaluatingBoardGameService.class)) {
            return;
        }

        if (event.isRetryAborted()) {
            log.error("Relevance retry exhausted after {} retries", MAX_RETRIES, event.getFailure());
        } else {
            log.warn("Answer was not relevant; another attempt will be made", event.getFailure());
        }
    }

    private void evaluateRelevancy(Question question, String answer) {
        var evaluationRequest = new EvaluationRequest(question.question(), answer);
        var evaluationResponse = evaluator.evaluate(evaluationRequest);
        if (!evaluationResponse.isPass()) {
            log.warn("Generated answer failed the relevance evaluation for question: {}", question.question());
            throw new AnswerNotRelevantException(question.question(), answer);
        }
    }
}
