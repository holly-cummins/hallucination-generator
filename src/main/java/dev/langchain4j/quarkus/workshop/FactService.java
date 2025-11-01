package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrails;
import jakarta.enterprise.context.SessionScoped;

@SessionScoped
@RegisterAiService
public interface FactService {

    @SystemMessage("""
            You are a helpful knowledge base.
            You are friendly, polite and concise.
            
            Be concise, with an answer of no more than 50 words.
            
            Today is {current_date}.
            """)
    @UserMessage("""
            Question: {question}.
            Some context:
            {context}
            """)
    @OutputGuardrails(HallucinationGuard.class)
    String chat(String question, String context);
}
