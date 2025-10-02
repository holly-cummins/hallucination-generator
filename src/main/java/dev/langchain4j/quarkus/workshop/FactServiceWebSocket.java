package dev.langchain4j.quarkus.workshop;

import io.quarkiverse.langchain4j.runtime.aiservice.GuardrailException;
import io.quarkus.logging.Log;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/facts")
public class FactServiceWebSocket {

    private final FactService FactService;

    public FactServiceWebSocket(FactService FactService) {
        this.FactService = FactService;
    }

    @OnOpen
    public String onOpen() {
        return "I know it all! How can I help you today?";
    }

    @OnTextMessage
    public String onTextMessage(String message) {
        try {
            return FactService.chat(message);
        } catch (GuardrailException e) {
            Log.errorf(e, "Error calling the LLM: %s", e.getMessage());
            return "Could not find an acceptable answer. " + e.getMessage();
        } catch (Exception e) {
            Log.errorf(e, "Error calling the LLM: %s", e.getMessage());
            return "Sorry, I am unable to process your request at the moment because someone standing in front of you made a coding error.";
        }
    }
}
