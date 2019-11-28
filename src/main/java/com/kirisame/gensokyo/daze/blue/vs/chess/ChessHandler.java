package com.kirisame.gensokyo.daze.blue.vs.chess;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 处理器
 * @auther: MaoHangBin
 * @date: 2019/11/27 15:29
 */

public class ChessHandler extends TextWebSocketHandler {

    private final int roomMapMaxSize = 2;

    private Map<String, Map<String, WebSocketSession>> roomsMap = new ConcurrentHashMap<>();

    /**
     * Map<sessionId, Map<roomName, userName>>
     **/
    private Map<String, Map<String, String>> placeMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomName = (String) session.getAttributes().get("roomName");
        String userName = (String) session.getAttributes().get("userName");
        Map<String, String> playerStayMap = new HashMap<>(1);
        playerStayMap.put("roomName", roomName);
        playerStayMap.put("userName", userName);
        Map<String, WebSocketSession> playersMap = roomsMap.get(roomName);
        if (playersMap == null) {
            playersMap = new ConcurrentHashMap<>(2);
            playersMap.put(userName, session);
            roomsMap.put(roomName, playersMap);
            placeMap.put(session.getId(), playerStayMap);
        } else {
            if (playersMap.size() > roomMapMaxSize - 1) {
                if (playersMap.get(userName) != null) {
                    return;
                }
                session.close();
            } else {
                playersMap.put(userName, session);
                placeMap.put(session.getId(), playerStayMap);
            }
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //获取发送过来的内容
        byte[] bytes = message.asBytes();
        String messageContent = new String(bytes);
        JSONObject jsonObject = JSONObject.parseObject(messageContent);
        //获取房间名
        String roomName = (String) jsonObject.get("roomName");
        //获取用户名
        String userName = (String) jsonObject.get("userName");
        //返回结果
        Map<String, WebSocketSession> playersMap = roomsMap.get(roomName);
        Set<String> keySet = playersMap.keySet();
        for (String playerName : keySet) {
            WebSocketSession playerSession = playersMap.get(playerName);
            if (playerSession != null && playerSession.isOpen()) {
                playerSession.sendMessage(new TextMessage(userName + " do click"));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, String> playerStayMap = placeMap.get(session.getId());
        if (playerStayMap != null && playerStayMap.size() > 0) {
            String roomName = playerStayMap.get("roomName");
            String userName = playerStayMap.get("userName");
            Map<String, WebSocketSession> playersMap = roomsMap.get(roomName);
            playersMap.remove(userName);
        }
        super.afterConnectionClosed(session, status);
    }
}
