package com.tibbertil.botforbibyt.service;

import com.tibbertil.botforbibyt.Logger;
import com.tibbertil.botforbibyt.entity.Level;
import com.tibbertil.botforbibyt.entity.LogEventEntity;
import com.tibbertil.botforbibyt.repository.LogEventRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BybitWebSocketService {

    private static final String WS_URL = "wss://stream.bybit.com/v5/public/linear";
    private WebSocketClient client;
    private final Logger logger;

    @PostConstruct
    public void start() {
        try {
            client = new WebSocketClient(new URI(WS_URL)) {

                @Override
                public void onOpen(ServerHandshake handshakedata) { // вызывается при успешном подключении к серверу;
                    logger.log(Level.INFO, "WebSocket opened", "WebSocketService");
                    subscribe();
                }

                @Override
                public void onMessage(String message) { //вызывается при получении сообщения с сервера;
                    logger.log(Level.INFO, message, "WebSocketService");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) { // вызывается при закрытии соединения;
                    logger.log(Level.WARN, "WebSocket closed: " + reason, "WebSocketService");
                }

                @Override
                public void onError(Exception ex) { // вызывается при ошибках.
                    logger.log(Level.ERROR,  ex.getMessage(), "WebSocketService");
                    ex.printStackTrace();
                }
            };

            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // строит JSON-запрос с операцией подписки "op": "subscribe" и аргументом — список каналов, на которые хочешь подписаться,
    // в данном случае "publicTrade.BTCUSDT" (публичные торги по паре BTC/USDT). Запрос превращается в строку и
    // отправляется через client.send() на сервер.
    private void subscribe() {
        JSONObject request = new JSONObject();
        request.put("op", "subscribe");
        request.put("args", List.of("publicTrade.BTCUSDT"));
        client.send(request.toString());
        logger.log(Level.INFO, "Subscribed to publicTrade.BTCUSDT", "WebSocketService");
    }
}
//При запуске приложения (@PostConstruct) — создаёт и запускает WebSocket-клиент.
//
//Подключается к Bybit WebSocket API — по адресу wss://stream.bybit.com/v5/public/linear.
//
//Подписывается на канал publicTrade.BTCUSDT — чтобы получать сделки по паре BTC/USDT.
//
//Логирует события:
//
//Успешное подключение (onOpen)
//
//Полученные сообщения (onMessage)
//
//Закрытие соединения (onClose)
//
//Ошибки (onError)
//
//Сохраняет логи в базу данных через LogEventRepository.