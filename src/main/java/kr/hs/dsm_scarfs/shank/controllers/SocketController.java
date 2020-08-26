package kr.hs.dsm_scarfs.shank.controllers;

import com.corundumstudio.socketio.SocketIOServer;
import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;
import kr.hs.dsm_scarfs.shank.service.socket.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class SocketController {

    private final SocketIOServer server;

    private final SocketService socketService;

    @PostConstruct
    public void setSocketMapping() {
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        String stringDate= DateFor.format(date);

        server.addConnectListener(client -> {
            System.out.printf(
                    "%s %s - %s - Socket Connected. Session Id: %s%n",
                    stringDate,
                    "SOCKET",
                    client.getRemoteAddress(),
                    client.getSessionId()
            );
        });

        server.addConnectListener(socketService::connect);

        server.addEventListener("send", MessageRequest.class,
                (client, data, ackSender) -> socketService.chat(client, data));

    }

}
