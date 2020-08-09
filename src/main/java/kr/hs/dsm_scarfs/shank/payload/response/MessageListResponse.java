package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageListResponse {

    private Integer userId;

    private String userNumber;

    private String userName;

    private String message;

    private LocalDateTime messageTime;

    private boolean isShow;
}
