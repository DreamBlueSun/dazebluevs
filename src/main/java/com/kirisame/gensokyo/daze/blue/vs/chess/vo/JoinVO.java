package com.kirisame.gensokyo.daze.blue.vs.chess.vo;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/11/30 0030 0:39
 */

public class JoinVO {

    /**
     * （消息类型（0：玩家加入，1：开启对局，2：落棋）
     **/
    private String messageType;
    /**
     * 加入房间是否成功（-1：已满（失败）,0：失败，1：成功）
     **/
    private String isJoined;
    /**
     * 加入的房间名称
     **/
    private String roomName;
    /**
     * 黑棋玩家名称
     **/
    private String blackName;
    /**
     * 白棋玩家名称
     **/
    private String whiteName;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getIsJoined() {
        return isJoined;
    }

    public void setIsJoined(String isJoined) {
        this.isJoined = isJoined;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getBlackName() {
        return blackName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }
}
