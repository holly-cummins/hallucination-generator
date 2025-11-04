package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class HallucinationGuard implements OutputGuardrail {

    @Inject
    ScoreWebSocket scoreWebSocket;

    private final HallucinationDetectionService service;

    public HallucinationGuard(HallucinationDetectionService service) {
        this.service = service;
    }

    @Override
    public OutputGuardrailResult validate(OutputGuardrailRequest params) {
        var responseFromLLM = params.responseFromLLM().aiMessage();
        if (responseFromLLM.text().isEmpty()) {
            System.out.println("Empty string, retrying");
            return retry("No response");
        }
        // Find the most recent user message to extract the question
        var prompt = params.requestParams().chatMemory().messages().reversed().stream().filter(cm -> cm.type().equals(ChatMessageType.USER)).findFirst();
        // Strip out the context from the question
        var question = prompt.isPresent() ? ((UserMessage) prompt.get()).singleText().split("Some context")[0] : "";
        double result = service.getLikelihood(question, responseFromLLM.text());
        scoreWebSocket.recordScore(result);

        System.out.println("Hallucination Guard: " + result);
        if (result > 0.5) {
            return retry("Truth detected. Most recent response: " + responseFromLLM.text());
        }
        return success();
    }
}
