package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApplicationListResponse {

    private Integer totalElements;

    private Integer totalPages;

    private Integer class_number;

    private List applicationResponses;

}
