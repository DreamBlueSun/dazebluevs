package com.kirisame.gensokyo.daze.blue.vs.chess.config;

import com.kirisame.gensokyo.daze.blue.vs.chess.ChessHandler;
import com.kirisame.gensokyo.daze.blue.vs.chess.ChessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @description: 配置
 * @auther: MaoHangBin
 * @date: 2019/11/27 15:14
 */
@Configuration
@EnableWebSocket
public class ChessSocketConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new ChessHandler(), "webSocket/*").addInterceptors(new ChessInterceptor());
    }
}
