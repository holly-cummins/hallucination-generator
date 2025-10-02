package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.guardrail.OutputGuardrails;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.SessionScoped;

@SessionScoped
@RegisterAiService
public interface FactService {

    @SystemMessage("""
            You are a helpful knowledge base.
            You are friendly, polite and concise.
            
            Be concise, but do include the question in your response.
            
            Today is {current_date}.
            """)
    @OutputGuardrails(HallucinationGuard.class)
    String chat(String userMessage);
}
