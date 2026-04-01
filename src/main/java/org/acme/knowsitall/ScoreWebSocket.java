package org.acme.knowsitall;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;

@WebSocket(path = "/scores")
public class ScoreWebSocket {

    private MultiEmitter<? super String> emitter;

    @OnOpen
    public Multi<String> onOpen() {
        return Multi.createFrom().<String>emitter(em -> {
            emitter = em;
            em.onTermination(() -> emitter = null);
        });
    }

    public void recordScore(double score) {
        if (emitter!=null) {
            emitter.emit(Math.round(score * 100) + "% truthy");
        }
    }

    public void recordEmpty() {
        if (emitter!=null) {
            emitter.emit("[ ]");
        }
    }

    public void recordStartOfCheck() {
        if (emitter!=null) {
            emitter.emit("...");
        }
    }

    public void recordReject(String text) {
        if (emitter!=null) {
            emitter.emit("Rejecting answer: " + text);
        }
    }
}
