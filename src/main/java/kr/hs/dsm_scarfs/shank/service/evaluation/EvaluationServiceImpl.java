package kr.hs.dsm_scarfs.shank.service.evaluation;

import kr.hs.dsm_scarfs.shank.entites.evaluation.team.TeamEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.team.repository.TeamEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.evaluation.personal.PersonalEvaluation;
import kr.hs.dsm_scarfs.shank.entites.evaluation.personal.repository.PersonalEvaluationRepository;
import kr.hs.dsm_scarfs.shank.entites.assignment.repository.AssignmentRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.exceptions.*;
import kr.hs.dsm_scarfs.shank.payload.request.TeamEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.request.PersonalEvaluationRequest;
import kr.hs.dsm_scarfs.shank.payload.response.EvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.PersonalEvaluationResponse;
import kr.hs.dsm_scarfs.shank.payload.response.TeamEvaluationInfo;
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
    private final PersonalEvaluationRepository personalEvaluationRepository;
    private final TeamEvaluationRepository teamEvaluationRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    @Override
    public void personalEvaluation(PersonalEvaluationRequest selfEvaluationRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        assignmentRepository.findById(selfEvaluationRequest.getAssignmentId())
                .orElseThrow(ApplicationNotFoundException::new);

        personalEvaluationRepository.findByAssignmentIdAndStudentId(selfEvaluationRequest.getAssignmentId(), student.getId())
                .ifPresent(personalEvaluation -> {throw new UserAlreadyEvaluationException();});

        personalEvaluationRepository.save(
                PersonalEvaluation.builder()
                    .assignmentId(selfEvaluationRequest.getAssignmentId())
                    .attitude(selfEvaluationRequest.getAttitude())
                    .communication(selfEvaluationRequest.getCommunication())
                    .scientificAccuracy(selfEvaluationRequest.getScientificAccuracy())
                    .createdAt(LocalDateTime.now())
                    .build()
        );
    }

    @Override
    public void teamEvaluation(TeamEvaluationRequest teamEvaluationRequest) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        Student target = studentRepository.findById(teamEvaluationRequest.getTargetId())
                .orElseThrow(TargetNotFoundException::new);

        assignmentRepository.findById(teamEvaluationRequest.getAssignmentId())
                .orElseThrow(ApplicationNotFoundException::new);

        Integer assignmentId = teamEvaluationRequest.getAssignmentId();
        Integer userId = student.getId();
        Integer targetId = target.getId();

        if (userId.equals(targetId)) throw new TargetNotFoundException();

        teamEvaluationRepository.findByAssignmentIdAndUserIdAndTargetId(assignmentId, userId, targetId)
                .ifPresent(teamEvaluation -> {throw new UserAlreadyEvaluationException();});

        teamEvaluationRepository.save(
                TeamEvaluation.builder()
                    .assignmentId(assignmentId)
                    .userId(userId)
                    .targetId(targetId)
                    .communication(teamEvaluationRequest.getCommunication())
                    .cooperation(teamEvaluationRequest.getCooperation())
                    .createdAt(LocalDateTime.now())
                    .build()
        );
    }

    @Override
    public List<EvaluationResponse> getEvaluationInfo(Integer assignmentId) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);

        assignmentRepository.findById(assignmentId)
                .orElseThrow(ApplicationNotFoundException::new);

        Member me = memberRepository.findByAssignmentIdAndStudentId(assignmentId, student.getId());
        List<Member> members = memberRepository.findAllByTeamIdAndStudentIdNot(me.getTeamId(), student.getId());
        List<EvaluationResponse> evaluationResponses = new ArrayList<>();

        evaluationResponses.add(
                EvaluationResponse.builder()
                    .studentId(student.getId())
                    .studentNumber(student.getStudentNumber())
                    .studentName(student.getName())
                    .isFinish(personalEvaluationRepository.existsByAssignmentIdAndStudentId(
                            assignmentId, student.getId()
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
                        .isFinish(teamEvaluationRepository.existsByAssignmentIdAndUserIdAndTargetId(
                                assignmentId, student.getId(), member.getStudentId()
                        ))
                        .build()
            );
        }

        return evaluationResponses;
    }

    @Override
    public PersonalEvaluationResponse personalEvaluationInfo(Integer assignmentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        PersonalEvaluation personalEvaluation = personalEvaluationRepository.findByAssignmentIdAndStudentId(assignmentId, user.getId())
                .orElseThrow(ApplicationNotFoundException::new);

        return PersonalEvaluationResponse.builder()
                    .attitude(personalEvaluation.getAttitude())
                    .communication(personalEvaluation.getCommunication())
                    .scientificAccuracy(personalEvaluation.getScientificAccuracy())
                    .createdAt(personalEvaluation.getCreatedAt())
                    .build();
    }

    @Override
    public TeamEvaluationInfo teamEvaluationInfo(Integer assignmentId, Integer targetId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        TeamEvaluation teamEvaluation =
                teamEvaluationRepository.findByAssignmentIdAndUserIdAndTargetId(assignmentId, user.getId(), targetId)
                        .orElseThrow(ApplicationNotFoundException::new);

        return TeamEvaluationInfo.builder()
                    .communication(teamEvaluation.getCommunication())
                    .cooperation(teamEvaluation.getCooperation())
                    .build();
    }

}
