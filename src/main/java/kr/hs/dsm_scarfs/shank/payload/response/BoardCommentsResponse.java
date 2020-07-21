package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardCommentsResponse {

    private Integer commentId;

    private String content;

    private String writerName;

    private LocalDateTime createdAt;

    private List<BoardCocommentsResponse> cocomments;

}