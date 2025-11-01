package dev.langchain4j.quarkus.workshop;

import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailParams;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
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
    public OutputGuardrailResult validate(OutputGuardrailParams params) {
        var responseFromLLM = params.responseFromLLM();
        var question = params.memory().messages().getLast();
        if (responseFromLLM.text().isEmpty()) {
            System.out.println("Empty string, retrying");
            return retry("No response");
        }
        double result = service.getLikelihood(question.toString(), responseFromLLM.text());
        scoreWebSocket.recordScore(result);

        System.out.println("Hallucination Guard: " + result);
        if (result > 0.5) {
            return retry("Truth detected. Most recent response: " + responseFromLLM.text());
        }
        return success();
    }
}
