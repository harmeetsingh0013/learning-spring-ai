package in.harmeetsingh.springaiplayground._03_rag.service;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Answer;
import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
//import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.FILTER_EXPRESSION;
import java.io.File;
import java.util.Map;

import static org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever.FILTER_EXPRESSION;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Service
public class RagSpringAIBoardGameService implements BoardGameService {
    private static final Logger log = LoggerFactory.getLogger(RagSpringAIBoardGameService.class);

    private final ChatClient chatClient;
    private final RagGameRulesService ragGameRulesService;

    public RagSpringAIBoardGameService(ChatClient chatClient, RagGameRulesService gameRulesService) {
        this.chatClient = chatClient;
        this.ragGameRulesService = gameRulesService;
    }

    @Value("classpath:/promptTemplates/advisorSystemPrompt.st")
    Resource advisorSystemPrompt;

    @Override
    public Answer askQuestion(Question question) {
        var gameRules = String.format("gameTitle == '%s'", ragGameRulesService.normalizeGameTitle(question.gameTitle()));

       var responseEntity = chatClient.prompt()
                .system(systemSpec ->
                        systemSpec.text(advisorSystemPrompt)
                        .param("gameTitle", question.gameTitle()))
                .user(userSpec ->
                        userSpec.text(question.question()))
               .advisors(advisorSpec ->
                       advisorSpec.params(Map.of(FILTER_EXPRESSION, gameRules, CONVERSATION_ID, "user-id")))
                .call()
                .responseEntity(Answer.class, ChatClient.EntityParamSpec::useProviderStructuredOutput);
       var answer = responseEntity.response();
        logUsage(answer.getMetadata().getUsage());
       return responseEntity.entity();
    }

    private void logUsage(Usage usage) {
        log.info("Token usage: prompt={}, generation={}, total={}",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
