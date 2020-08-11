package kr.hs.dsm_scarfs.shank.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HomeworkResponse {

    private Integer homeworkId;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private String preViewContent;

    private HomeworkType type;

    private boolean isComplete;

    private Integer view;

}
