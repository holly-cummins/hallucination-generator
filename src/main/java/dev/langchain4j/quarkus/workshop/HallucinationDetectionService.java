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
             Score the following statement on both how factually correct it is and how relevant it is to the question. Score between 0 and 1, where 0 is total fabrication and 1 is provably correct.
            
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
            
             Example 4:
             Question: Is wine good for you?
             Statement: Yes, wine is good for you; it provides a rich flavor while also being high in calories and alcohol.
             0.3
            
             Example 5: 
             Question: Is water good for you?
             Statement: No, according to the information provided, water from an automated dispenser at the food station is being used instead of a real source.
             0.4
            
             Example 6:
             Question: Is water good for you?
             Statement: Yes, water is essential for maintaining body temperature and protecting vital organs. Water also helps keep the gastrointestinal tract empty to prevent constipation and promotes waste removal.
             0.7
            
             Example 7:
             Question: How many rs in strawberry?
             Statement: Strawberry has 20 rs.
             0.1
            
             Example 8:
             Question: How many rs in strawberry?
             Statement: The question "How many RS in strawberry?" is a bit of a trick question! There isn’t a standard unit of measurement for strawberry size. However, if we’re going by the context, it likely refers to “Root System Score,” which is a metric used in agriculture to assess strawberry health. Therefore, a rough estimate could be around 100 RS.
             0.1
            
             Example 9:
             Question: How many rs in the word strawberry?
             Statement: There are six Rs in the word “strawberry.”
             0.2
            
             Example 10:
             Question: how many rs in the word strawberry?
             Statement: There are three Rs in “strawberry.”
             1.0
            
             Question: {question}
             Statement: {responseFromLLM}
            """)
    double getLikelihood(String question, String responseFromLLM);
}
