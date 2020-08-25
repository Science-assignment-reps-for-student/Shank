package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/message")
@RequiredArgsConstructor
public class SocketController {

    private final MessageService messageService;

    @GetMapping
    public List<MessageListResponse> getMessageList() {
        return messageService.getMessageList();
    }

    @GetMapping("/{userId}")
    public List<MessageResponse> getChats(@PathVariable Integer userId) {
        return messageService.getChats(userId);
    }

    @PostMapping("/{messageId}")
    public void readMessage(@PathVariable Integer messageId) {
        messageService.readMessage(messageId);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
    }

    @MessageMapping("/send/{studentId}/{adminId}")
    @SendTo({"/receive/{studentId}/{adminId}", "/receive/admin"})
    public MessageResponse sendToAdmin(@DestinationVariable Integer studentId,
                                       @DestinationVariable Integer adminId,
                                       MessageRequest messageRequest) {
        return messageService.chat(studentId, adminId, messageRequest);
    }

}
