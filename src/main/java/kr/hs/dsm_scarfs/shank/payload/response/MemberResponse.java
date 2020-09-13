package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private final Integer memberId;
    private final String memberName;
    private final String memberNumber;
}
