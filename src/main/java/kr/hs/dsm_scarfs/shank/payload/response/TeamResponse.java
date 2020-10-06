package kr.hs.dsm_scarfs.shank.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TeamResponse {

    private Integer teamId;

    private String teamName;

    private Integer leaderId;

    private String leaderName;

    private boolean isLeader;

    private List<MemberResponse> memberList;

}
