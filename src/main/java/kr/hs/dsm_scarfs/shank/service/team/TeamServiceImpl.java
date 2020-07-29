package kr.hs.dsm_scarfs.shank.service.team;

import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.Team;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.exceptions.*;
import kr.hs.dsm_scarfs.shank.payload.request.TeamRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MemberResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TeamResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService{

    private final AuthenticationFacade authenticationFacade;

    private final TeamRepository teamRepository;
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final MemberRepository memberRepository;

    @Override
    public TeamResponse getTeam(Integer homeworkId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        homeworkRepository.findById(homeworkId)
                .orElseThrow(ApplicationNotFoundException::new);

        Team team = teamRepository.findByLeaderIdAndHomeworkId(student.getId(), homeworkId)
                .orElseThrow(TeamNotFoundException::new);

        Student leader = studentRepository.findById(team.getLeaderId())
                .orElseThrow(TeamLeaderNotFoundException::new);

        List<MemberResponse> memberResponses = new ArrayList<>();

        for (Member member : memberRepository.findAllByTeamId(team.getId())) {
            if (member.getId().equals(leader.getId())) continue;
            Student memberStudent = studentRepository.findById(member.getId())
                    .orElseThrow(MemberNotFoundException::new);

            memberResponses.add(
                    MemberResponse.builder()
                        .memberId(memberStudent.getId())
                        .memberName(memberStudent.getName())
                        .build()
            );
        }


        return TeamResponse.builder()
                    .teamId(team.getId())
                    .teamName(team.getName())
                    .leaderId(leader.getId())
                    .leaderName(leader.getName())
                    .memberList(memberResponses)
                    .build();
    }

    @Override
    public void addTeam(TeamRequest teamRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        homeworkRepository.findById(teamRequest.getHomeworkId())
                .orElseThrow(ApplicationNotFoundException::new);

        Team team = teamRepository.save(
                Team.builder()
                    .homeworkId(teamRequest.getHomeworkId())
                    .name(teamRequest.getTeamName())
                    .leaderId(student.getId())
                    .build()
        );

        memberRepository.save(
                Member.builder()
                    .teamId(team.getId())
                    .studentId(student.getId())
                    .homeworkId(team.getHomeworkId())
                    .build()
        );
    }

    @Override
    public void deleteTeam(Integer teamId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        if (!student.getId().equals(team.getLeaderId())) throw new UserNotLeaderException();

        memberRepository.deleteAllByTeamId(teamId);
        teamRepository.delete(team);
    }

}
