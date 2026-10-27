package in.harmeetsingh.springaiplayground._01_basics.service;

import in.harmeetsingh.springaiplayground._01_basics.models.Answer;
import in.harmeetsingh.springaiplayground._01_basics.models.Question;
import org.springframework.ai.chat.client.ChatClient;

//@Component
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
