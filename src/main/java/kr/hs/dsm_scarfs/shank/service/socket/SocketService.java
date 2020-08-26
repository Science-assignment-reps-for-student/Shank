package kr.hs.dsm_scarfs.shank.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;

public interface SocketService {
    void connect(SocketIOClient client);
    void chat(SocketIOClient client, MessageRequest messageRequest);
}
