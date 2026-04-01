package org.acme.knowsitall;

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
        scoreWebSocket.recordStartOfCheck();
        var responseFromLLM = params.responseFromLLM().aiMessage();
        if (responseFromLLM.text()==null || responseFromLLM.text().isEmpty()) {
            System.out.println("Empty string, retrying\n" + responseFromLLM);
            scoreWebSocket.recordEmpty();
            return retry("No response");
        }
        // Find the most recent user message to extract the question
        var prompt = params.requestParams().chatMemory().messages().reversed().stream().filter(cm -> cm.type().equals(ChatMessageType.USER)).findFirst();
        // Strip out the context from the question
        var question = prompt.isPresent() ? ((UserMessage) prompt.get()).singleText().split("Some context")[0]:"";
        double truthiness = service.getLikelihood(question, responseFromLLM.text());

        scoreWebSocket.recordScore(truthiness);

        System.out.println("Hallucination Guard: " + truthiness);
        if (truthiness < 0.5) {
            scoreWebSocket.recordReject(responseFromLLM.text());
            return reprompt("Truth detected. Most recent response: " + responseFromLLM.text(), "Ignore the previous messages and answer this question: " + question);
        }
        return success();
    }
}
