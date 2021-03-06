package kr.hs.dsm_scarfs.shank.service.team;

import kr.hs.dsm_scarfs.shank.entites.assignment.repository.AssignmentRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.repository.MutualEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.repository.SelfEvaluationRepository;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final AuthenticationFacade authenticationFacade;

    private final TeamRepository teamRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final MemberRepository memberRepository;
    private final SelfEvaluationRepository selfEvaluationRepository;
    private final MutualEvaluationRepository mutualEvaluationRepository;

    @Override
    public TeamResponse getTeam(Integer assignmentId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        assignmentRepository.findById(assignmentId)
                .orElseThrow(ApplicationNotFoundException::new);

        Member me = memberRepository.findByStudentIdAndAssignmentId(student.getId(), assignmentId)
                .orElseThrow(TeamNotFoundException::new);

        Team team = teamRepository.findById(me.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        Student leader = studentRepository.findById(team.getLeaderId())
                .orElseThrow(TeamLeaderNotFoundException::new);

        List<MemberResponse> memberResponses = new ArrayList<>();

        for (Member member : memberRepository.findAllByTeamId(team.getId())) {
            if (member.getStudentId().equals(leader.getId())) continue;
            Student memberStudent = studentRepository.findById(member.getStudentId())
                    .orElseThrow(MemberNotFoundException::new);

            memberResponses.add(
                    MemberResponse.builder()
                        .memberId(member.getId())
                        .memberName(memberStudent.getName())
                        .memberNumber(memberStudent.getStudentNumber())
                        .build()
            );
        }


        return TeamResponse.builder()
                    .teamId(team.getId())
                    .teamName(team.getName())
                    .leaderId(leader.getId())
                    .leaderName(leader.getName())
                    .isLeader(student.getId().equals(leader.getId()))
                    .memberList(memberResponses)
                    .build();
    }

    @Override
    public void addTeam(TeamRequest teamRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        assignmentRepository.findById(teamRequest.getAssignmentId())
                .orElseThrow(ApplicationNotFoundException::new);

        memberRepository.findByStudentIdAndAssignmentId(student.getId(), teamRequest.getAssignmentId())
                .ifPresent(member -> {
                    throw new UserAlreadyIncludeException();
                });

//        teamRepository.findByAssignmentIdAndName(teamRequest.getAssignmentId(), teamRequest.getTeamName())
//                .ifPresent(team -> {
//                    throw new TeamAlreadyExistsException();
//                });

        Team team = teamRepository.save(
                Team.builder()
                    .assignmentId(teamRequest.getAssignmentId())
                    .name(teamRequest.getTeamName())
                    .leaderId(student.getId())
                    .build()
        );

        memberRepository.save(
                Member.builder()
                    .teamId(team.getId())
                    .studentId(student.getId())
                    .assignmentId(team.getAssignmentId())
                    .build()
        );
    }

    @Override
    @Transactional
    public void deleteTeam(Integer teamId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        if (!student.getId().equals(team.getLeaderId())) throw new UserNotLeaderException();

        for (Member member : memberRepository.findAllByTeamId(teamId)) {
            selfEvaluationRepository.deleteByStudentIdAndAssignmentId(member.getStudentId(), team.getAssignmentId());
            mutualEvaluationRepository.deleteAllByStudentIdAndAssignmentId(member.getStudentId(), team.getAssignmentId());
        }
        selfEvaluationRepository.deleteByStudentIdAndAssignmentId(student.getId(), team.getAssignmentId());
        memberRepository.deleteAllByTeamId(teamId);
        teamRepository.delete(team);
    }

}
