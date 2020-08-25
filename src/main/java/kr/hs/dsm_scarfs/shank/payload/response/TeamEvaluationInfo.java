package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamEvaluationInfo {

    private Integer cooperation;

    private Integer communication;
}
