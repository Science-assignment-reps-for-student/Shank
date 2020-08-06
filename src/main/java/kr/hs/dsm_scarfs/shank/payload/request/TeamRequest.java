package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    private Integer homeworkId;

    private String teamName;
}
