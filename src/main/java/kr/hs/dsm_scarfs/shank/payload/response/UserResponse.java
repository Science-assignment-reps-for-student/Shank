package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private String name;

    private String studentNumber;

    private Integer remainingAssignment;

    private Integer completionAssignment;
}
