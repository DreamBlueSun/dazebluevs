package com.kirisame.gensokyo.daze.blue.vs.chess;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 处理器
 * @auther: MaoHangBin
 * @date: 2019/11/27 15:29
 */

public class ChessHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> clientMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userName = (String) session.getAttributes().get("userName");
        clientMap.put(userName, session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //获取发送过来的内容
        byte[] bytes = message.asBytes();
        String messageContent = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(messageContent);
        //获取接收者
        String to = (String) jsonObject.get("to");
        //获取接收者
        WebSocketSession session1 = clientMap.get(to);
        //获取发送者
        String from = (String) session.getAttributes().get("userName");
        //要发送的消息
        String content = (String) jsonObject.get("content");
        if (session1 != null && session1.isOpen()) {
            //发送消息
            session1.sendMessage(new TextMessage("收到" + from + "发送的消息,内容是:====>" + content));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
