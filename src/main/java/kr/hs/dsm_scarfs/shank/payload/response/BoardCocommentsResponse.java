package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardCocommentsResponse {

    private Integer cocommentId;

    private String content;

    private String writerName;

    private LocalDateTime createdAt;

}
