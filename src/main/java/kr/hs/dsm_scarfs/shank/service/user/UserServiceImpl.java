package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.entites.authcode.repository.AuthCodeRepository;
import kr.hs.dsm_scarfs.shank.entites.file.multi.repository.MultiFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.sigle.repository.SingleFileRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.team.Team;
import kr.hs.dsm_scarfs.shank.entites.team.repository.TeamRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationStatus;
import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.UserResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final EmailService emailService;

    private final StudentRepository studentRepository;
    private final AuthCodeRepository authCodeRepository;
    private final EmailVerificationRepository verificationRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final AuthenticationFacade authenticationFacade;
    private final SingleFileRepository singleFileRepository;
    private final MultiFileRepository multiFileRepository;
    private final HomeworkRepository homeworkRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;


    @Override
    public void signUp(SignUpRequest signUpRequest) {
        studentRepository.findByStudentNumber(signUpRequest.getNumber()).ifPresent(student -> {
            throw new RuntimeException();
        });

       verificationRepository.findById(signUpRequest.getEmail())
               .filter(EmailVerification::isVerified)
               .orElseThrow(RuntimeException::new);

       authCodeRepository.findByStudentNumberAndCode(signUpRequest.getNumber(), signUpRequest.getAuthCode())
               .orElseThrow(RuntimeException::new);

       studentRepository.save(
               Student.builder()
                   .email(signUpRequest.getEmail())
                   .studentNumber(signUpRequest.getNumber())
                   .password(signUpRequest.getPassword())
                   .name(signUpRequest.getName())
                   .build()
       );
    }

    @Override
    public void sendEmail(String email) {
        studentRepository.findByEmail(email).ifPresent(student -> {
            throw new RuntimeException();
        });

        String code = randomCode();
        emailService.sendEmail(email, code);
        verificationRepository.save(
                EmailVerification.builder()
                    .email(email)
                    .code(code)
                    .status(EmailVerificationStatus.UNVERIFIED)
                    .build()
        );
    }

    @Override
    public void verifyEmail(VerifyCodeRequest verifyCodeRequest) {
        String email = verifyCodeRequest.getEmail();
        String code = verifyCodeRequest.getCode();
        EmailVerification emailVerification = emailVerificationRepository.findById(email)
                .orElseThrow(RuntimeException::new);

        if (!emailVerification.getCode().equals(code))
            throw new RuntimeException();

        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }

    @Override
    public UserResponse getUser() {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        int remainingAssignment = 0, completionAssignment = 0;


        for (Homework homework : homeworkRepository.findHomework(student.getStudentNumber())) {
            if (homework.getType().equals(MULTI)) {
                Member member = getCurrentMember(student.getId(), homework.getId()).orElseThrow(RuntimeException::new);
                if (multiFileRepository.existsByHomeworkIdAndTeamId(homework.getId(), member.getTeamId()))
                    completionAssignment++;
                else
                    remainingAssignment++;
            } else {
                if (singleFileRepository.existsByHomeworkIdAndUserId(homework.getId(), student.getId()))
                    completionAssignment++;
                else
                    remainingAssignment++;
            }
        }
        return UserResponse.builder()
                .name(student.getName())
                .studentNumber(student.getStudentNumber())
                .completionAssignment(completionAssignment)
                .remainingAssignment(remainingAssignment)
                .build();
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");

        for (int i = 0; i < 6; i++) {
            result.append(codes[(int) (Math.random() % codes.length)]);
        }
        return result.toString();
    }

    public Optional<Member> getCurrentMember(Integer id, Integer homeworkId) {
        List<Team> teams = teamRepository.findByHomeworkId(homeworkId).orElseGet(ArrayList::new);
        List<Integer> teamIds = new ArrayList<>();
        for (Team team : teams) teamIds.add(team.getId());

        return memberRepository.findByUserIdAndTeamIdIn(id, teamIds);
    }

}
