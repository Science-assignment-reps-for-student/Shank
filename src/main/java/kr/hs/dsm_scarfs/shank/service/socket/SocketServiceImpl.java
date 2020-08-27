package kr.hs.dsm_scarfs.shank.service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import kr.hs.dsm_scarfs.shank.entites.message.Message;
import kr.hs.dsm_scarfs.shank.entites.message.repository.MessageRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.InvalidTokenException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SocketServiceImpl implements SocketService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserFactory userFactory;

    private final MessageRepository messageRepository;

    private final SocketIOServer server;

    @Override
    public void connect(SocketIOClient client) {
        int studentId;
        int adminId;
        try {
            studentId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("studentId"));
            adminId = Integer.parseInt(client.getHandshakeData().getSingleUrlParam("adminId"));
        } catch (NumberFormatException e) {
            client.disconnect();
            return;
        }
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if (!jwtTokenProvider.validateToken(token)) {
            client.disconnect();
            return;
        }
        User user = userFactory.getUser(jwtTokenProvider.getUserEmail(token));

        client.set("user", user);
        if (user.getType().equals(AuthorityType.STUDENT) && user.getId().equals(studentId)) {
            client.joinRoom(studentId + ":" + adminId);
        } else if (user.getType().equals(AuthorityType.ADMIN) && user.getId().equals(adminId)) {
            client.joinRoom(studentId + ":" + adminId);
        } else {
            client.disconnect();
            return;
        }

        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        String stringDate= DateFor.format(date);

        System.out.printf(
                "%s  %s - %s - Socket Connected. Session Id: %s%n",
                stringDate,
                "SOCKET",
                client.getRemoteAddress(),
                client.getSessionId()
        );
    }

    @Override
    public void chat(SocketIOClient client, MessageRequest messageRequest) {
        int studentId = 0;
        int adminId = 0;
        for (String room : client.getAllRooms()) {
            String[] splitRoom = room.split(":");
            studentId = Integer.parseInt(splitRoom[0]);
            adminId = Integer.parseInt(splitRoom[1]);
        }
        if (studentId == 0 || adminId == 0) {
            client.disconnect();
            return;
        }
        User user = client.get("user");
        if (!user.getId().equals(studentId) && !user.getId().equals(adminId))
            throw new PermissionDeniedException();

        Message message = messageRepository.save(
                Message.builder()
                        .studentId(studentId)
                        .adminId(adminId)
                        .message(messageRequest.getMessage())
                        .time(LocalDateTime.now())
                        .isDeleted(false)
                        .isShow(false)
                        .build()
        );

        server.getRoomOperations(studentId + ":" + adminId).sendEvent("receive", MessageResponse.builder()
                .id(message.getId())
                .message(message.getMessage())
                .time(message.getTime())
                .type(message.getType())
                .build());
    }

}
