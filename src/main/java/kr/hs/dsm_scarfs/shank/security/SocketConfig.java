package kr.hs.dsm_scarfs.shank.security;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketConfig {

    @Bean
    public SocketIOServer webSocketServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setOrigin("*:*");
        config.setPort(8001);

        return new SocketIOServer(config);
    }

}
