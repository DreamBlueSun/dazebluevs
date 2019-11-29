package com.kirisame.gensokyo.daze.blue.vs.chess.entity;

import org.springframework.web.socket.WebSocketSession;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/11/30 0030 0:36
 */

public class Player {

    /**
     * 玩家所在房间
     **/
    private String roomName;
    /**
     * 玩家名称
     **/
    private String userName;
    /**
     * 玩家类型（0：白棋，1：黑棋）
     **/
    private String userType;
    /**
     * 玩家会话
     **/
    private WebSocketSession session;

    public Player() {
    }

    public Player(String roomName, String userName, String userType, WebSocketSession session) {
        this.roomName = roomName;
        this.userName = userName;
        this.userType = userType;
        this.session = session;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }
}
