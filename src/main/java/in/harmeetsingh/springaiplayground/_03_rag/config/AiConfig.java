package in.harmeetsingh.springaiplayground._03_rag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;

@Configuration
public class AiConfig {

//    @Bean
//    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
//        var chatOptions = ChatOptions.builder()
//                .model("gpt-4o-mini");
//        return chatClientBuilder
//                .defaultOptions(chatOptions)
//                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore).build(), new SimpleLoggerAdvisor())
//                .build();
//    }

    @Bean
    public ChatClient ragAdvisorChatClient(ChatClient.Builder chatClientBuilder, VectorStore vectorStore, ChatMemory chatMemory) {
        var chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini");
        var ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore).build()).build();

        var chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(chatMemory).build();

        return chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor(), ragAdvisor, chatMemoryAdvisor)
                .defaultOptions(chatOptions)
                .build();
    }
}
