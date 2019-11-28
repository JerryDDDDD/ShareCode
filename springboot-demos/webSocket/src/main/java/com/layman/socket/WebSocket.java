package com.layman.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @ClassName WebSocket
 * @Description TODO
 * @Author 叶泽文
 * @Data 2019/11/14 10:30
 * @Version 3.0
 **/
@ServerEndpoint(value = "/7DGroup")
@Component
public class WebSocket {

    private Logger logger = LoggerFactory.getLogger(WebSocket.class);

    //连接时执行
    @OnOpen
    public void onOpen(Session session) {
        logger.info("WebSocket Open: "+ session.getId());
    }

    //关闭时执行
    @OnClose
    public void onClose(CloseReason reason) {
        logger.info("Closing a WebSocket due to "+ reason.getReasonPhrase());
    }

    //收到消息时执行
    @OnMessage
    public String onMessage(String message) {
        logger.info("Received : "+ message);
        return message;
    }

    //连接错误时执行
    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.debug(session.getId() + " > OnError: " + throwable.getMessage());
    }
}
