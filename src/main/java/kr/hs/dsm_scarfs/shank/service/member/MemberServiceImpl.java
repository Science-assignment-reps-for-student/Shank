package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.Team;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final AuthenticationFacade authenticationFacade;

    private final StudentRepository studentRepository;
    private final HomeworkRepository homeworkRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Override
    public void setMember(MemberRequest memberRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        Team team = teamRepository.findById(memberRequest.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(RuntimeException::new);


        memberRepository.findByStudentIdAndHomeworkId(memberRequest.getTargetId(), team.getHomeworkId())
                .ifPresent(member -> { throw new RuntimeException();});

        memberRepository.save(
                Member.builder()
                    .homeworkId(team.getHomeworkId())
                    .studentId(memberRequest.getTargetId())
                    .teamId(team.getId())
                    .build()
        );
    }

    @Override
    public void deleteMember(Integer targetId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        Member member = memberRepository.findById(targetId)
                .orElseThrow(RuntimeException::new);

        teamRepository.findById(member.getTeamId())
                .filter(t -> student.getId().equals(t.getLeaderId()))
                .orElseThrow(RuntimeException::new);

        memberRepository.delete(member);
    }

}
