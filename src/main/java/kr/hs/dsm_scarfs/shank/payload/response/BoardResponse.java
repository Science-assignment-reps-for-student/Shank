package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BoardResponse {

    private Integer boardId;

    private String title;

    private String name;

    private LocalDate createdAt;

    private Integer view;
}
