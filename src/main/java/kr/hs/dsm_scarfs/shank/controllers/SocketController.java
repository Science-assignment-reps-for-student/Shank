package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final MessageService messageService;

    @GetMapping("/message")
    public List<MessageListResponse> getMessageList() {
        return messageService.getMessageList();
    }

    @GetMapping("/message/{userId}")
    public List<MessageResponse> getChats(@PathVariable Integer userId) {
        return messageService.getChats(userId);
    }

    @PostMapping("/message/{messageId}")
    public void readMessage(@PathVariable Integer messageId) {
        messageService.readMessage(messageId);
    }

    @MessageMapping("/send/{userId}")
    @SendTo({"/receive/{userId}", "/receive/admin"})
    public MessageResponse chat(@DestinationVariable Integer userId, MessageRequest messageRequest) {
        return messageService.chat(userId, messageRequest);
    }
}
