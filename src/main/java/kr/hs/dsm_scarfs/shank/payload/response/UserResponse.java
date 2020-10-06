package kr.hs.dsm_scarfs.shank.payload.response;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {

    private Integer id;

    private String name;

    private String studentNumber;

    private AuthorityType type;

    private Integer remainingAssignment;

    private Integer completionAssignment;

}
