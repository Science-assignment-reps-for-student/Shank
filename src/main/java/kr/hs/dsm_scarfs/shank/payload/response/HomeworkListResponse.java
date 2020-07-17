package kr.hs.dsm_scarfs.shank.payload.response;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HomeworkListResponse {

    private int totalElements;

    private int totalPages;

    private List<HomeworkResponse> homeworkResponses;

}
