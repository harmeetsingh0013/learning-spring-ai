package in.harmeetsingh.springaiplayground;

import in.harmeetsingh.springaiplayground._01_basics.models.Question;
import in.harmeetsingh.springaiplayground._01_basics.service.BoardGameService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringAIBoardGameServiceTest {

    @Autowired
    private BoardGameService boardGameService;

    @Autowired
    private ChatClient.Builder chatBuilder;

    private RelevancyEvaluator relevancyEvaluator;
    private FactCheckingEvaluator factCheckingEvaluator;

    @BeforeEach
    public void setup() {
        this.relevancyEvaluator = new RelevancyEvaluator(chatBuilder);
        this.factCheckingEvaluator = FactCheckingEvaluator.builder(chatBuilder)
                .evaluationPrompt("""
                        You are an unyielding fact-checking auditor.
                        You should only pass the claim with concrete and close answers.
                        You should not consider unnecessary long description, which doesn't make any sense.
                        """).build();
    }

    @Test
    public void evaluateRelevancy() {
        var userText = "why is the sky blue?";
        var question = new Question(userText);
        var answer = boardGameService.askQuestion(question);

        var evaluationRequest = new EvaluationRequest(userText, answer.answer());
        var evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

        Assertions.assertThat(evaluationResponse.isPass())
                .withFailMessage("""
                        ========================================
                        The answer "%s"
                        is not considered relevant to the question
                        "%s".
                        ========================================
                        """, answer.answer(), userText)
                .isTrue();
    }
}
