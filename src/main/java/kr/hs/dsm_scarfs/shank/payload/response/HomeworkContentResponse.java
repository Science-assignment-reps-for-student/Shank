package kr.hs.dsm_scarfs.shank.payload.response;

import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class HomeworkContentResponse {

    private String title;

    private HomeworkType type;

    private LocalDateTime createdAt;

    private LocalDate deadLine;

    private String content;

    private Integer view;

    private boolean isComplete;

}
