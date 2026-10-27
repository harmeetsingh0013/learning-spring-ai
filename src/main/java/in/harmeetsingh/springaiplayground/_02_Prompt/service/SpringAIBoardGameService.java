package in.harmeetsingh.springaiplayground._02_Prompt.service;

import in.harmeetsingh.springaiplayground._02_Prompt.models.Answer;
import in.harmeetsingh.springaiplayground._02_Prompt.models.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SpringAIBoardGameService implements BoardGameService {
    private static final Logger log = LoggerFactory.getLogger(SpringAIBoardGameService.class);

    private final ChatClient chatClient;
    private final GameRulesService gameRulesService;

    public SpringAIBoardGameService(ChatClient.Builder chatClientBuilder, GameRulesService gameRulesService) {
        var chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini");
        this.chatClient = chatClientBuilder.defaultOptions(chatOptions).build();
        this.gameRulesService = gameRulesService;
    }

    @Value("classpath:/promptTemplates/systemPromptRelatedToGameTemplate.st")
    Resource questionRelatedTOGameTemplate;

    @Override
    public Answer askQuestion(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());

       var responseEntity = chatClient.prompt()
                .system(systemSpec ->
                        systemSpec.text(questionRelatedTOGameTemplate)
                        .param("gameTitle", question.gameTitle())
                        .param("rules", gameRules))
                .user(userSpec ->
                        userSpec.text(question.question()))
                .call()
                .responseEntity(Answer.class, ChatClient.EntityParamSpec::useProviderStructuredOutput);
       var answer = responseEntity.response();
        logUsage(answer.getMetadata().getUsage());
       return responseEntity.entity();
    }

    @Override
    public Flux<String> askQuestionInStream(Question question) {
        var gameRules = gameRulesService.getRulesFor(question.gameTitle());

        return chatClient.prompt()
                .system(systemSpec ->
                        systemSpec.text(questionRelatedTOGameTemplate)
                                .param("gameTitle", question.gameTitle())
                                .param("rules", gameRules))
                .user(userSpec ->
                        userSpec.text(question.question()))
                .stream()
                .content();
    }

    private void logUsage(Usage usage) {
        log.info("Token usage: prompt={}, generation={}, total={}",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
