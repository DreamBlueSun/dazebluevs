package com.kirisame.gensokyo.daze.blue.vs.chess;

import com.alibaba.fastjson.JSONObject;
import com.kirisame.gensokyo.daze.blue.frame.redis.client.RedisClientInterface;
import com.kirisame.gensokyo.daze.blue.util.SpringUtils;
import com.kirisame.gensokyo.daze.blue.vs.chess.entity.Player;
import com.kirisame.gensokyo.daze.blue.vs.chess.util.FiveInARowUtils;
import com.kirisame.gensokyo.daze.blue.vs.chess.vo.DoPointVO;
import com.kirisame.gensokyo.daze.blue.vs.chess.vo.JoinVO;
import com.kirisame.gensokyo.daze.blue.vs.chess.vo.StartGameVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 处理器
 * @auther: MaoHangBin
 * @date: 2019/11/27 15:29
 */

public class ChessHandler extends TextWebSocketHandler {

    private RedisClientInterface redisClient = SpringUtils.getBean(RedisClientInterface.class);

    private final int roomMapMaxSize = 2;

    /**
     * Map<roomName, List<Player>>
     **/
    private Map<String, List<Player>> roomsMap = new ConcurrentHashMap<>();
    /**
     * Map<sessionId, Player>
     **/
    private Map<String, Player> sessionPlayerMap = new ConcurrentHashMap<>();
    /**
     * List<startedRoomName>
     **/
    private Set<String> startedRoomSet = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomName = (String) session.getAttributes().get("roomName");
        String userName = (String) session.getAttributes().get("userName");
        //返回加入房间的玩家的信息
        JoinVO joinVO = new JoinVO();
        joinVO.setMessageType("0");
        joinVO.setRoomName(roomName);
        List<Player> playerList = roomsMap.get(roomName);
        if (playerList == null) {
            //创建房间
            playerList = new ArrayList<>();
            roomsMap.put(roomName, playerList);
            //加入房间
            Player player = new Player(roomName, userName, "1", session);
            playerList.add(player);
            sessionPlayerMap.put(session.getId(), player);
            joinVO.setIsJoined("1");
            //房主为黑方
            joinVO.setBlackName(userName);
            joinVO.setWhiteName("");
            sendMessageToPlayerList(roomName, joinVO);
        } else if (playerList.size() < roomMapMaxSize) {
            //校验房间内重名
            if (!StringUtils.equals(roomsMap.get(roomName).get(0).getUserName(), userName)) {
                //加入房间
                Player player = new Player(roomName, userName, "0", session);
                playerList.add(player);
                sessionPlayerMap.put(session.getId(), player);
                joinVO.setIsJoined("1");
                //第二人为白方
                joinVO.setBlackName(playerList.get(0).getUserName());
                joinVO.setWhiteName(userName);
                sendMessageToPlayerList(roomName, joinVO);
            } else {
                joinVO.setIsJoined("0");
                String jsonString = JSONObject.toJSONString(joinVO);
                TextMessage textMessage = new TextMessage(jsonString);
                sendMessageToPlayer(session, textMessage);
                session.close();
            }
        } else {
            joinVO.setIsJoined("-1");
            String jsonString = JSONObject.toJSONString(joinVO);
            TextMessage textMessage = new TextMessage(jsonString);
            sendMessageToPlayer(session, textMessage);
            session.close();
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            //获取发送过来的内容
            byte[] bytes = message.asBytes();
            String messageContent = new String(bytes);
            JSONObject jsonObject = JSONObject.parseObject(messageContent);
            //获取动作类型
            String actionType = (String) jsonObject.get("actionType");
            switch (actionType) {
                case "0":
                    doStart(jsonObject, session);
                    break;
                case "1":
                    doPoint(jsonObject, session);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doStart(JSONObject jsonObject, WebSocketSession session) {
        try {
            //获取房间名
            String roomName = (String) jsonObject.get("roomName");
            //用户类型（0：白棋，1：黑棋）
            String userType = getUserType(roomName, session.getId());
            //开启对局
            String isStart = "0";
            if (StringUtils.equals(userType, "1")) {
                isStart = "1";
                startedRoomSet.add(roomName);
            }
            //返回结果
            StartGameVO startGameVO = new StartGameVO();
            startGameVO.setMessageType("1");
            startGameVO.setIsStart(isStart);
            sendMessageToPlayerList(roomName, startGameVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doPoint(JSONObject jsonObject, WebSocketSession session) {
        Jedis jedis = null;
        try {
            //获取房间名
            String roomName = (String) jsonObject.get("roomName");
            if (!startedRoomSet.contains(roomName)) {
                DoPointVO doPointVO = new DoPointVO();
                doPointVO.setMessageType("2");
                doPointVO.setIsWin("-1");
                sendMessageToPlayerList(roomName, doPointVO);
            } else {
                //获取落点
                String doPoint = (String) jsonObject.get("doPoint");
                //用户类型（0：白棋，1：黑棋）
                String userType = getUserType(roomName, session.getId());
                //处理落点
                jedis = redisClient.getJedis();
                redisClient.hSet("FIVE_IN_A_ROW_CHESS_" + roomName, doPoint, userType, jedis);
                Map<String, String> pointAllMap = redisClient.hGetAll("FIVE_IN_A_ROW_CHESS_" + roomName, jedis);
                boolean win = FiveInARowUtils.isWin(doPoint, userType, pointAllMap);
                String isWin = win ? "1" : "0";
                //返回结果
                DoPointVO doPointVO = new DoPointVO();
                doPointVO.setMessageType("2");
                doPointVO.setIsWin(isWin);
                doPointVO.setUserType(userType);
                doPointVO.setDoPoint(doPoint);
                sendMessageToPlayerList(roomName, doPointVO);
                if (win) {
                    redisClient.del("FIVE_IN_A_ROW_CHESS_" + roomName, jedis);
                    roomsMap.remove(roomName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisClient.closeJedis(jedis);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Player player = sessionPlayerMap.get(session.getId());
        if (player != null) {
            sessionPlayerMap.remove(session.getId());
            List<Player> playerList = roomsMap.get(player.getRoomName());
            if (playerList != null && playerList.size() > 0) {
                playerList.remove(player);
                if (playerList.size() == 0) {
                    roomsMap.remove(player.getRoomName());
                } else {
                    playerList.get(0).setUserType("1");
                }
            }
        }
        super.afterConnectionClosed(session, status);
    }

    private String getUserType(String roomName, String sessionId) {
        String userType = "-1";
        List<Player> playerList = roomsMap.get(roomName);
        for (Player player : playerList) {
            if (StringUtils.equals(sessionId, player.getSession().getId())) {
                userType = player.getUserType();
            }
        }
        return userType;
    }

    private void sendMessageToPlayerList(String roomName, Object vo) {
        String jsonString = JSONObject.toJSONString(vo);
        TextMessage textMessage = new TextMessage(jsonString);
        List<Player> playerList = roomsMap.get(roomName);
        for (Player player : playerList) {
            try {
                player.getSession().sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessageToPlayer(WebSocketSession session, TextMessage textMessage) {
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
