package com.crayon.netty.client.websocket.server;

import com.crayon.netty.client.websocket.config.WebSocketClientProperties;
import com.crayon.netty.client.websocket.config.WebsocketClientAction;
import com.crayon.netty.client.websocket.config.WebsocketClientManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty;
import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * TODO
 *
 * @Description
 * @Author Administrator
 * @Date 2025/3/28 21:48
 **/
@Slf4j
@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WebSocketConnect {

    private final WebSocketClientProperties webSocketClientProperties;

    public void connectServer(WebsocketClientAction websocketClientAction) {
        if (WebsocketClientManager.getRECONNECT()) {
            return ;
        }
        if (isNull(websocketClientAction)) {
            return;
        }
        WebsocketClientManager.setWebsocketClientAction(websocketClientAction);
        List<WebSocketClientProperties.WebSocketUrl> webSocketUrlList = webSocketClientProperties.getWebSocketUrlList();
        if (isEmpty(webSocketUrlList)) {
            return;
        }
        Set<String> usedUriList = new HashSet<>();
        webSocketUrlList.forEach(webSocketUrl -> usedUriList.add(webSocketUrl.getUrl() + ":" + webSocketUrl.getPort() + webSocketUrl.getPath()));
        if (isNotEmpty(WebsocketClientManager.getServiceUriList())) {
            usedUriList.removeAll(WebsocketClientManager.getServiceUriList());
            if (isEmpty(usedUriList)) {
                return;
            }
        }
        if (isNotEmpty(usedUriList)) {
            usedUriList.forEach(uri -> Thread.ofVirtual().start(() -> {
                try {
                    connectToClient(websocketClientAction, uri);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }

    private void connectToClient(WebsocketClientAction websocketClientAction, String uri) throws Exception {
        WebSocketServer client = new WebSocketServer(websocketClientAction, uri, webSocketClientProperties);
        client.init();
        client.connect();
//        client.shutdown();
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    public void refreshCheck() {
        connectServer(WebsocketClientManager.getWebsocketClientAction());
    }

}
