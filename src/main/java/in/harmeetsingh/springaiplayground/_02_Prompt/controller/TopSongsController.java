package in.harmeetsingh.springaiplayground._02_Prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
public class TopSongsController {

    @Value("classpath:/top-songs-prompt.st")
    Resource topSongsPromptTemplate;

    private final ChatClient chatClient;

    public TopSongsController(ChatClient.Builder chatClientBuilder) {
        var chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini");

        this.chatClient = chatClientBuilder.defaultOptions(chatOptions).build();
    }

    @GetMapping(path="/topSongs", produces = "application/json")
    public List<String> topSongs(@RequestParam("year") String year) {
        return chatClient.prompt()
                .user(userSpec ->
                        userSpec.text(topSongsPromptTemplate)
                                .param("year", year))
                .call()
                .entity(new ParameterizedTypeReference<List<String>>() {});
    }
}
