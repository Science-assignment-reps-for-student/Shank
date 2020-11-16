package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    private Integer assignmentId;

    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|\\*]+$")
    private String teamName;

}
