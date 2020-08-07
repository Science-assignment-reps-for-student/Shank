package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {

    private Integer teamId;

    private Integer targetId;
}
