package com.kirisame.gensokyo.daze.blue.vs.chess;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.net.URLDecoder;
import java.util.Map;

/**
 * @description: 拦截器
 * @auther: MaoHangBin
 * @date: 2019/11/27 15:19
 */

public class ChessInterceptor extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        //截取请求Url最后参数作为用户名
        String requestUrl = request.getURI().toString();
        String userName = StringUtils.substring(requestUrl, requestUrl.lastIndexOf("/") + 1);
        //截取请求Url最后参数上一个作为房间名
        String urlExcludeUserName = StringUtils.substring(requestUrl, 0, requestUrl.lastIndexOf("/"));
        String roomName = StringUtils.substring(urlExcludeUserName, urlExcludeUserName.lastIndexOf("/") + 1);
        //放入attributes
        String userNameDecode = URLDecoder.decode(userName, "UTF-8");
        attributes.put("roomName", roomName);
        attributes.put("userName", userNameDecode);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
