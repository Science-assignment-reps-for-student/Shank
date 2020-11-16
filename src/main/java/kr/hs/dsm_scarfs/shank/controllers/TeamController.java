package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.TeamRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TeamResponse;
import kr.hs.dsm_scarfs.shank.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{homeworkId}")
    public TeamResponse getTeam(@PathVariable Integer homeworkId) {
        return teamService.getTeam(homeworkId);
    }

    @PostMapping
    public void addTeam(@RequestBody @Valid TeamRequest teamRequest) {
        teamService.addTeam(teamRequest);
    }

    @DeleteMapping("/{teamId}")
    public void deleteTeam(@PathVariable Integer teamId) {
        teamService.deleteTeam(teamId);
    }

}
