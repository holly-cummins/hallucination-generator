package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService
public interface StoryGenerator {


    @UserMessage("Generate a story about {topic}. The story must be at least 100 words long")
    String generate(String topic);
}
