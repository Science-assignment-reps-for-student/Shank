package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {

    private Integer noticeId;

    private String title;

    private LocalDateTime createdAt;

    private String preViewContent;

    private Integer view;

}
