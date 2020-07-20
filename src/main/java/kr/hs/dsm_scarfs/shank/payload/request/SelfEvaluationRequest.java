package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;

@Getter
public class SelfEvaluationRequest {

    private Integer homeworkId;

    private Integer scientificAccuracy;

    private Integer communication;

    private Integer attitude;

}
