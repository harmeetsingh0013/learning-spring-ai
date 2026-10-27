package in.harmeetsingh.springaiplayground.service;

import in.harmeetsingh.springaiplayground.models.Answer;
import in.harmeetsingh.springaiplayground.models.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class SpringAIBoardGameService implements BoardGameService{
    private final ChatClient chatClient;

    public SpringAIBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        return new Answer(chatClient.prompt()
                .user(question.question())
                .call()
                .content());
    }
}
