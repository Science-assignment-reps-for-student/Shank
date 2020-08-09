package kr.hs.dsm_scarfs.shank.service.message;

import kr.hs.dsm_scarfs.shank.entites.message.Message;
import kr.hs.dsm_scarfs.shank.entites.message.enums.AuthorityType;
import kr.hs.dsm_scarfs.shank.entites.message.repository.MessageRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.MessageNotFoundException;
import kr.hs.dsm_scarfs.shank.payload.request.MessageRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final UserFactory userFactory;
    private final AuthenticationFacade authenticationFacade;

    private final MessageRepository messageRepository;

    @Override
    public List<MessageListResponse> getMessageList() {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        List<MessageListResponse> messageListResponses = new ArrayList<>();

        if (user.getType().equals(AuthorityType.STUDENT)) {
            messageRepository.findFirstByStudentIdOrderByTimeDesc(user.getId())
                    .ifPresent(message -> messageListResponses.add(
                            MessageListResponse.builder()
                                .userId(user.getId())
                                .userNumber(user.getStudentNumber())
                                .userName(user.getName())
                                .message(message.getMessage())
                                .messageTime(message.getTime())
                            .build()
                    ));
        }
        return null;
    }

    @Override
    public List<MessageResponse> getChats(Integer targetId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<Message> messages = messageRepository
                .findAllByUserIdAndTargetIdAndTypeOrderByTimeAsc(user.getId(), targetId, user.getType());

        List<MessageRepository> messageRepositories = new ArrayList<>();
    }

    @Override
    public void readMessage(Integer messageId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        Message message = messageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (user.getType().equals(message.getType())) return;

        message.setShow(true);
        messageRepository.save(message);
    }

    @Override
    public MessageResponse chat(Integer userId, MessageRequest messageRequest) {
        return null;
    }
}
