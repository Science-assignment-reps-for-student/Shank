package kr.hs.dsm_scarfs.shank.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BoardResponse {

    private Integer boardId;

    private String title;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate createdAt;

    private Integer view;
}
