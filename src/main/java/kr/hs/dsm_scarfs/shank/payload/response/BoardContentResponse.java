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

    private String nextBoardTitle;

    private String preBoardTitle;

    private Integer nextBoardId;

    private Integer preBoardId;

    private List<String> images;

    private List<BoardCommentsResponse> comments;

}
