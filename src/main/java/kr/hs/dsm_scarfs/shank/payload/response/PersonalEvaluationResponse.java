package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PersonalEvaluationResponse {

    private Integer scientificAccuracy;

    private Integer communication;

    private Integer attitude;

    private LocalDateTime createdAt;

}
