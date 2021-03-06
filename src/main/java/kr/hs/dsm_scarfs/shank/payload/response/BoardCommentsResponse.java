package kr.hs.dsm_scarfs.shank.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class BoardCommentsResponse {

    private Integer commentId;

    private String studentNumber;

    private String content;

    private String writerName;

    private AuthorityType type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private boolean isMine;

    private List<BoardCocommentsResponse> cocomments;

}
