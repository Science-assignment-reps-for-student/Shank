package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeContentResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private Integer view;

}
