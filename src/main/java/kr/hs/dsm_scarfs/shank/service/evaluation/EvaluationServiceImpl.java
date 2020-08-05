package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.MutualEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.mutual.repository.MutualEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.SelfEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.self.repository.SelfEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.exceptions.*;
import kr.hs.dsm_scarfs.shank.payload.request.MutualEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.SelfEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.SelfEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TargetEvaluationInfo;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final UserFactory userFactory;
    private final AuthenticationFacade authenticationFacade;

    private final StudentRepository studentRepository;
    private final SelfEvaluationRepository selfEvaluationRepository;
    private final MutualEvaluationRepository mutualEvaluationRepository;
    private final HomeworkRepository homeworkRepository;
    private final MemberRepository memberRepository;

    @Override
    public void selfEvaluation(SelfEvaluationRequest selfEvaluationRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        homeworkRepository.findById(selfEvaluationRequest.getHomeworkId())
                .orElseThrow(ApplicationNotFoundException::new);

        selfEvaluationRepository.findByHomeworkIdAndStudentId(selfEvaluationRequest.getHomeworkId(), student.getId())
                .ifPresent(selfEvaluation -> {throw new UserAlreadyEvaluationException();});

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
                .orElseThrow(UserNotFoundException::new);

        Student target = studentRepository.findById(mutualEvaluationRequest.getTargetId())
                .orElseThrow(TargetNotFoundException::new);

        homeworkRepository.findById(mutualEvaluationRequest.getHomeworkId())
                .orElseThrow(ApplicationNotFoundException::new);

        Integer homeworkId = mutualEvaluationRequest.getHomeworkId();
        Integer userId = student.getId();
        Integer targetId = target.getId();

        if (userId.equals(targetId)) throw new TargetNotFoundException();

        mutualEvaluationRepository.findByHomeworkIdAndUserIdAndTargetId(homeworkId, userId, targetId)
                .ifPresent(mutualEvaluation -> {throw new UserAlreadyEvaluationException();});

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
                .orElseThrow(UserNotFoundException::new);

        homeworkRepository.findById(homeworkId)
                .orElseThrow(ApplicationNotFoundException::new);

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
                    .orElseThrow(MemberNotFoundException::new);

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

    @Override
    public SelfEvaluationResponse selfEvaluationInfo(Integer homeworkId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        SelfEvaluation selfEvaluation = selfEvaluationRepository.findByHomeworkIdAndStudentId(homeworkId, user.getId())
                .orElseThrow(ApplicationNotFoundException::new);

        return SelfEvaluationResponse.builder()
                    .attitude(selfEvaluation.getAttitude())
                    .communication(selfEvaluation.getCommunication())
                    .scientificAccuracy(selfEvaluation.getScientificAccuracy())
                    .createdAt(selfEvaluation.getCreatedAt())
                    .build();
    }

    @Override
    public TargetEvaluationInfo targetEvaluationInfo(Integer homeworkId, Integer targetId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        MutualEvaluation mutualEvaluation =
                mutualEvaluationRepository.findByHomeworkIdAndUserIdAndTargetId(homeworkId, user.getId(), targetId)
                        .orElseThrow(ApplicationNotFoundException::new);

        return TargetEvaluationInfo.builder()
                    .communication(mutualEvaluation.getCommunication())
                    .cooperation(mutualEvaluation.getCooperation())
                    .build();
    }

}
