package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSearchResponse {
    private final Integer id;
    private final String number;
    private final String name;
}
