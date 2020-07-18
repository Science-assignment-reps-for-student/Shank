package kr.hs.dsm_scarfs.shank.service.team;

import kr.hs.dsm_scarfs.shank.payload.request.TeamRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TeamResponse;

public interface TeamService {
    TeamResponse getTeam(Integer homeworkId);
    void addTeam(TeamRequest teamRequest);
    void deleteTeam(Integer teamId);

}
