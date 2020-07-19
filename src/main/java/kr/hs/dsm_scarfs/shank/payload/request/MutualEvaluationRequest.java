package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;


@Getter
public class MutualEvaluationRequest {

    private Integer homeworkId;

    private Integer targetId;

    private Integer cooperation;

    private Integer communication;

}
