package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.Team;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.exceptions.MemberAlreadyIncludeException;
import kr.hs.dsm_scarfs.shank.exceptions.MemberNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.TeamNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotLeaderException;
import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MemberSearchResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final StudentRepository studentRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;

    @Override
    public void setMember(MemberRequest memberRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotLeaderException::new);

        Team team = teamRepository.findById(memberRequest.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(TeamNotFoundException::new);


        memberRepository.findByStudentIdAndAssignmentId(memberRequest.getTargetId(), team.getAssignmentId())
                .ifPresent(member -> { throw new MemberAlreadyIncludeException();});

        memberRepository.save(
                Member.builder()
                    .assignmentId(team.getAssignmentId())
                    .studentId(memberRequest.getTargetId())
                    .teamId(team.getId())
                    .build()
        );
    }

    @Override
    public void deleteMember(Integer targetId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotLeaderException::new);

        Member member = memberRepository.findById(targetId)
                .orElseThrow(MemberNotFoundException::new);

        teamRepository.findById(member.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(TeamNotFoundException::new);

        memberRepository.delete(member);
    }

    @Override
    public List<MemberSearchResponse> searchMember(String query) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<MemberSearchResponse> responses = new ArrayList<>();
        for (Student student : studentRepository.findAllByNameContainsOrStudentNumberContains(query, query)) {
            if (user.getId().equals(student.getId())) continue;

            responses.add(
                    MemberSearchResponse.builder()
                        .id(student.getId())
                        .number(student.getStudentNumber())
                        .name(student.getName())
                        .build()
            );
        }

        return responses;
    }

}
