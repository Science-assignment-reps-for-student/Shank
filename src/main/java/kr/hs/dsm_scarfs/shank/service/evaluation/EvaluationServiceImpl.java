package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.MutualEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.repository.MutualEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.SelfEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.repository.SelfEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final AuthenticationFacade authenticationFacade;

    private final StudentRepository studentRepository;
    private final SelfEvaluationRepository selfEvaluationRepository;
    private final MutualEvaluationRepository mutualEvaluationRepository;
    private final HomeworkRepository homeworkRepository;
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Override
    public void selfEvaluation(SelfEvaluationRequest selfEvaluationRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        homeworkRepository.findById(selfEvaluationRequest.getHomeworkId())
                .orElseThrow(RuntimeException::new);

        selfEvaluationRepository.findByHomeworkIdAndStudentId(selfEvaluationRequest.getHomeworkId(), student.getId())
                .ifPresent(selfEvaluation -> {throw new RuntimeException();});

        selfEvaluationRepository.save(
                SelfEvaluation.builder()
                    .homeworkId(selfEvaluationRequest.getHomeworkId())
                    .attitude(selfEvaluationRequest.getAttitude())
                    .communication(selfEvaluationRequest.getCommunication())
                    .scientificAccuracy(selfEvaluationRequest.getScientificAccuracy())
                    .createdAt(LocalDateTime.now())
                    .build()
        );
    }

    @Override
    public void mutualEvaluation(MutualEvaluationRequest mutualEvaluationRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        Student target = studentRepository.findById(mutualEvaluationRequest.getTargetId())
                .orElseThrow(RuntimeException::new);

        homeworkRepository.findById(mutualEvaluationRequest.getHomeworkId())
                .orElseThrow(RuntimeException::new);

        Integer homeworkId = mutualEvaluationRequest.getHomeworkId();
        Integer userId = student.getId();
        Integer targetId = target.getId();

        if (userId.equals(targetId)) throw new RuntimeException();

        mutualEvaluationRepository.findByHomeworkIdAndUserIdAndTargetId(homeworkId, userId, targetId)
                .ifPresent(mutualEvaluation -> {throw new RuntimeException();});

        mutualEvaluationRepository.save(
                MutualEvaluation.builder()
                    .homeworkId(homeworkId)
                    .userId(userId)
                    .targetId(targetId)
                    .communication(mutualEvaluationRequest.getCommunication())
                    .cooperation(mutualEvaluationRequest.getCooperation())
                    .createdAt(LocalDateTime.now())
                    .build()
        );
    }

    @Override
    public List<EvaluationResponse> getEvaluationInfo(Integer homeworkId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        homeworkRepository.findById(homeworkId)
                .orElseThrow(RuntimeException::new);

        Member me = memberRepository.findByHomeworkIdAndStudentId(homeworkId, student.getId());
        List<Member> members = memberRepository.findAllByTeamIdAndStudentIdNot(me.getTeamId(), student.getId());
        List<EvaluationResponse> evaluationResponses = new ArrayList<>();

        evaluationResponses.add(
                EvaluationResponse.builder()
                    .studentId(student.getId())
                    .studentNumber(student.getStudentNumber())
                    .studentName(student.getName())
                    .isFinish(selfEvaluationRepository.existsByHomeworkIdAndStudentId(
                            homeworkId, student.getId()
                    ))
                    .build()

        );
        for (Member member : members) {
            Student memberStudent = studentRepository.findById(member.getStudentId())
                    .orElseThrow(RuntimeException::new);

            evaluationResponses.add(
                    EvaluationResponse.builder()
                        .studentId(memberStudent.getId())
                        .studentNumber(memberStudent.getStudentNumber())
                        .studentName(memberStudent.getName())
                        .isFinish(mutualEvaluationRepository.existsByHomeworkIdAndUserIdAndTargetId(
                                homeworkId, student.getId(), member.getStudentId()
                        ))
                        .build()
            );
        }

        return evaluationResponses;
    }

}
