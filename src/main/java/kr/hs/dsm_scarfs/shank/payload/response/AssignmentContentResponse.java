package kr.hs.dsm_scarfs.shank.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hs.dsm_scarfs.shank.entites.assignment.enums.HomeworkType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class AssignmentContentResponse {

    private String title;

    private HomeworkType type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private LocalDate deadLine;

    private String content;

    private Integer view;

    private String nextBoardTitle;

    private String preBoardTitle;

    private Integer nextBoardId;

    private Integer preBoardId;

    private boolean isComplete;

}
