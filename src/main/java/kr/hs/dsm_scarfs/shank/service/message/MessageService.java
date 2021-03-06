package kr.hs.dsm_scarfs.shank.service.message;

import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageListResponse> getMessageList();
    List<MessageResponse> getChats(Integer userId);
    void readMessage(Integer messageId);
    void deleteMessage(Integer messageId);
}
