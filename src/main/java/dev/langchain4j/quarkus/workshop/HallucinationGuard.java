package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.data.message.AiMessage;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HallucinationGuard implements OutputGuardrail {

    private final HallucinationDetectionService service;

    public HallucinationGuard(HallucinationDetectionService service) {
        this.service = service;
    }

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        if (responseFromLLM.text().length() < 1) {
            System.out.println("Empty string, retrying");
            return retry("No response");
        }
        double result = service.getLikelihood(responseFromLLM.text());
        System.out.println("Hallucination Guard: " + result);
        if (result > 0.5) {
            return retry("Truth detected. Most recent response: " + responseFromLLM.text());
        }
        return success();
    }
}
