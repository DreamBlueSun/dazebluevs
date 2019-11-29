package com.kirisame.gensokyo.daze.blue.vs.chess.vo;

/**
 * @description:
 * @auther: MaoHangBin
 * @date: 2019/11/30 0030 3:13
 */

public class StartGameVO {

    /**
     * （消息类型（0：玩家加入，1：开启对局，2：落棋）
     **/
    private String messageType;
    /**
     * 是否开启了对局（0：未开启，1：开启）
     **/
    private String isStart;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
    }
}
