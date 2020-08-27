package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelfEvaluationRequest {

    private Integer assignmentId;

    private Integer scientificAccuracy;

    private Integer communication;

    private Integer attitude;

}
