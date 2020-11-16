package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.repository.MutualEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.repository.SelfEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.Team;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.exceptions.*;
import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final SelfEvaluationRepository selfEvaluationRepository;
    private final MutualEvaluationRepository mutualEvaluationRepository;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public void addMember(MemberRequest memberRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotLeaderException::new);

        Student target = studentRepository.findById(memberRequest.getTargetId())
                .orElseThrow(MemberNotFoundException::new);

        Team team = teamRepository.findById(memberRequest.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(TeamNotFoundException::new);


        memberRepository.findByStudentIdAndAssignmentId(target.getId(), team.getAssignmentId())
                .ifPresent(member -> {
                    throw new MemberAlreadyIncludeException();
                });

        memberRepository.save(
                Member.builder()
                    .assignmentId(team.getAssignmentId())
                    .studentId(target.getId())
                    .teamId(team.getId())
                    .build()
        );
    }

    @Override
    @Transactional
    public void deleteMember(Integer targetId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotLeaderException::new);

        Member member = memberRepository.findById(targetId)
                .orElseThrow(MemberNotFoundException::new);

        if (student.getId().equals(targetId))
            throw new InvalidTargetException();

        Team team = teamRepository.findById(member.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(TeamNotFoundException::new);

        selfEvaluationRepository.deleteByStudentIdAndAssignmentId(member.getStudentId(), team.getAssignmentId());
        mutualEvaluationRepository.deleteAllByStudentIdAndAssignmentId(member.getStudentId(), team.getAssignmentId());
        mutualEvaluationRepository.deleteAllByAssignmentIdAndTargetId(team.getAssignmentId(), member.getStudentId());
        memberRepository.delete(member);
    }

}
