package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(modelName = "validator")
@ApplicationScoped
public interface HallucinationDetectionService {

    @SystemMessage("""
            You are a fact detection system. You will validate whether a statement is likely to be true. Validation does not require external data access.
            """)
    @UserMessage("""
             Score the following statement on how factually correct it is. Score between 0 and 1, where 0 is total fabrication and 1 is provably correct.
            
             Only return a single number. Do not return anything else. Do not even return a newline or a leading field. Only a single floating point number.
            
             Example 1:
             Statement: The moon is made of cheese.
             0.2
            
             Example 2:
             Statement: The chemical formula of water is H2O
             1.0
            
             Example 3:
             Statement: Strawberries are a vegetable
             0.1
            
             Statement: {responseFromLLM}
            """)
    double getLikelihood(String responseFromLLM);
}
