package kr.hs.dsm_scarfs.shank.service.message;

import kr.hs.dsm_scarfs.shank.entites.message.Message;
import kr.hs.dsm_scarfs.shank.entites.message.repository.MessageRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.MessageNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.payload.response.MessageListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.MessageResponse;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
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

        for (User targetUser : userFactory.getChattedMessageList(user)) {
            int[] sortedId = userFactory.getSortedId(user, targetUser);
            Message message = messageRepository.findFirstByStudentIdAndAdminIdOrderByTimeDesc(
                    sortedId[0], sortedId[1]
            ).orElseThrow(MessageNotFoundException::new);

            User sender = userFactory.getMessageSender(message);
            messageListResponses.add(
                    MessageListResponse.builder()
                        .userId(targetUser.getId())
                        .userNumber(targetUser.getStudentNumber())
                        .userName(targetUser.getName())
                        .message(message.getMessage())
                        .messageTime(message.getTime())
                        .isShow(user.getType().equals(message.getType()) || message.isShow())
                        .isDeleted(message.isDeleted())
                        .isMine(user.equals(sender))
                        .build()
            );
        }

        return messageListResponses;
    }

    @Override
    public List<MessageResponse> getChats(Integer targetId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<Message> messages;
        if (user.getType().equals(AuthorityType.STUDENT))
            messages = messageRepository
                    .findAllByStudentIdAndAdminIdOrderByTimeAsc(user.getId(), targetId);
        else
            messages = messageRepository
                    .findAllByStudentIdAndAdminIdOrderByTimeAsc(targetId, user.getId());

        List<MessageResponse> messageResponses = new ArrayList<>();
        for (Message message : messages) {
            User sender = userFactory.getMessageSender(message);
            messageResponses.add(
                    MessageResponse.builder()
                        .id(message.getId())
                        .message(message.getMessage())
                        .time(message.getTime())
                        .type(message.getType())
                        .isDeleted(message.isDeleted())
                        .isMine(user.equals(sender))
                        .build()
            );
            if (!user.getType().equals(message.getType()))
                messageRepository.save(message.read());
        }

        return messageResponses;
    }

    @Override
    public void readMessage(Integer messageId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        Message message = messageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if (user.getType().equals(message.getType())) return;

        messageRepository.save(message.read());
    }

    @Override
    public void deleteMessage(Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(MessageNotFoundException::new);

        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        if (user.getType().equals(AuthorityType.STUDENT) && !user.getId().equals(message.getStudentId()))
            throw new PermissionDeniedException();
        else if (user.getType().equals(AuthorityType.ADMIN) && !user.getId().equals(message.getAdminId()))
            throw new PermissionDeniedException();

        messageRepository.save(message.delete());
    }

}
