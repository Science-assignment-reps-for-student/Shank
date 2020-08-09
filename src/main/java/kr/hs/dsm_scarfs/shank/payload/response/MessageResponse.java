package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponse {

    private Integer messageId;

    private String message;

    private Integer messageType;

    private Long messageTime;

    private String errorMessage;
}
