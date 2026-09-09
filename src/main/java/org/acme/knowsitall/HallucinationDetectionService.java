package org.acme.knowsitall;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.RequestScoped;

@RegisterAiService(modelName = "validator")
@RequestScoped
public interface HallucinationDetectionService {

    @SystemMessage("""
            You are an objective, expert AI evaluator.
            Your task is to assess the quality of an AI-generated answer based on a specific user question. 
            ### Evaluation Criteria: 
            - Accuracy: Does the answer contain correct information without hallucinations? 
            - Completeness: Does it fully address all parts of the user's prompt? 
            ### Scoring Scale: 
            - 2 points: Completely accurate and fully complete. 
            - 1 point: Mostly accurate or partially complete, with minor omissions. 
            - 0 points: Inaccurate, irrelevant, or missing key information. 
            ### Instructions: 1. Analyze the user question and the provided answer. 
            2. Assign a final score based on the scoring scale above. 
            
            Only return a number, nothing else!
            """)
    @UserMessage("""
            --- User Question: {question} AI Answer: {responseFromLLM}
            """)
    double getLikelihood(String question, String responseFromLLM);
}
