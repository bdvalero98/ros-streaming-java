package com.rosstreaming.interfaces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rosstreaming.application.GetInterfaceTrafficUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TrafficWebSocketHandler extends TextWebSocketHandler {

    private final GetInterfaceTrafficUseCase getInterfaceTrafficUseCase = null;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        sessions.removeIf(s -> !s.isOpen());
    }

    {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> {
                    if (!sessions.isEmpty()) {
                        getInterfaceTrafficUseCase.execute().ifPresent(
                                traffic -> {
                                    try {
                                        String json = objectMapper.writeValueAsString(traffic);

                                        for (WebSocketSession session : sessions) {
                                            if (session.isOpen()) {
                                                session.sendMessage(new TextMessage(json));
                                            }
                                        }
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
                                }
                        );
                    }
                }, 1, 2, TimeUnit.SECONDS
        );
    }
}
