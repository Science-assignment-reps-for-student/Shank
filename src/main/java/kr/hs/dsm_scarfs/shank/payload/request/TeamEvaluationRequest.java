package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamEvaluationRequest {

    private Integer assignmentId;

    private Integer targetId;

    private Integer cooperation;

    private Integer communication;

}
