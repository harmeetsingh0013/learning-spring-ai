package in.harmeetsingh.springaiplayground._03_rag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RagGameRulesService {

    private final VectorStore vectorStore;

    public RagGameRulesService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String getRulesFor(String gameName, String question) {
        var searchRequest = SearchRequest
                .builder()
                .query(question)
                .similarityThreshold(0.5)
                .topK(6)
                .filterExpression(
                        new FilterExpressionBuilder()
                                .eq("gameTitle", normalizeGameTitle(gameName))
                                .build()
                ).build();

        System.out.println("Search Request: "+ searchRequest);

        var similarDocs =
                vectorStore.similaritySearch(searchRequest);
        if (similarDocs.isEmpty()) {
            return "The rules for " + gameName + " are not available";
        }

        return similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public String normalizeGameTitle(String gameName) {
        return gameName.toLowerCase().replace(" ", "_");
    }
}
