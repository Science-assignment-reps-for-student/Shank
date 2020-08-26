package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

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

}
