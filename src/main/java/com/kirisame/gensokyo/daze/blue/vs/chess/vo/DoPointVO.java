package com.kirisame.gensokyo.daze.blue.vs.chess.vo;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/11/30 0030 0:41
 */

public class DoPointVO {

    /**
     * （消息类型（0：玩家加入，1：开启对局，2：落棋）
     **/
    private String messageType;
    /**
     * 是否获胜（-1：已结束的对局，0：未获胜，1：获胜）
     **/
    private String isWin;
    /**
     * 玩家类型（0：白棋，1：黑棋）
     **/
    private String userType;
    /**
     * 落点
     **/
    private String doPoint;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDoPoint() {
        return doPoint;
    }

    public void setDoPoint(String doPoint) {
        this.doPoint = doPoint;
    }
}
