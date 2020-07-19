package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EvaluationResponse {

    private Integer studentId;

    private String studentNumber;

    private String studentName;

    private boolean isFinish;

}
