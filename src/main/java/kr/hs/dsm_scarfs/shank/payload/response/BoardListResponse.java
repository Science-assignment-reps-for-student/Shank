package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardListResponse {

    private int totalElements;

    private int totalPages;

    private List<BoardResponse> boardResponses;
}
