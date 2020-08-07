package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TargetEvaluationInfo {

    private Integer cooperation;

    private Integer communication;
}
