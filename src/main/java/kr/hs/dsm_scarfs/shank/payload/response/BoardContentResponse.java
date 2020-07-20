package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class BoardContentResponse {

    private String title;

    private String writerName;

    private LocalDate createdAt;

    private Integer view;

    private String content;

    private String nextBoardName;

    private String preBoardName;

    private Integer nextBoardNumber;

    private Integer preBoardNumber;

    private List<BoardCommentsResponse> comments;

}
