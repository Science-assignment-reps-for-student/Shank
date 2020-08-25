package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.entites.authcode.repository.AuthCodeRepository;
import kr.hs.dsm_scarfs.shank.entites.file.multi.repository.MultiFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.sigle.repository.SingleFileRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationStatus;
import kr.hs.dsm_scarfs.shank.exceptions.*;
import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.UserResponse;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Random;


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
    private final MemberRepository memberRepository;

    private final UserFactory userFactory;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        studentRepository.findByStudentNumber(signUpRequest.getNumber()).ifPresent(student -> {
                throw new NumberDuplicationException();
        });

       verificationRepository.findById(signUpRequest.getEmail())
               .filter(EmailVerification::isVerified)
               .orElseThrow(InvalidAuthEmailException::new);

       authCodeRepository.findByStudentNumberAndCode(signUpRequest.getNumber(), signUpRequest.getAuthCode())
               .orElseThrow(InvalidAuthCodeException::new);

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
            throw new UserNotFoundException();
        });

        String code = randomCode();
        System.out.println(code);
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
                .orElseThrow(InvalidAuthEmailException::new);

        if (!emailVerification.getCode().equals(code))
            throw new InvalidAuthCodeException();

        emailVerificationRepository.save(emailVerification.verify());
    }

    @SneakyThrows
    @Override
    public UserResponse getUser(Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        int remainingAssignment = 0, completionAssignment = 0;

        String methodName = "findAllByDeadline" + user.getStudentClassNumber() + "After";
        Page<Homework> homeworkPage = (Page<Homework>) HomeworkRepository.class
                .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                .invoke(homeworkRepository, page, LocalDate.now(ZoneId.of("Asia/Seoul")));

        for (Homework homework : homeworkPage) {
            if (user.getType().equals(AuthorityType.ADMIN)) break;
            Optional<Member> member = memberRepository.findByStudentIdAndHomeworkId(user.getId(), homework.getId());

            if (homework.getType().equals(HomeworkType.MULTI)) {
                if (member.isPresent()) {
                    if (multiFileRepository.existsByHomeworkIdAndTeamId(homework.getId(), member.get().getTeamId())) {
                        completionAssignment++;
                        continue;
                    }
                }

                remainingAssignment++;
            } else {
                if (singleFileRepository.existsByHomeworkIdAndUserId(homework.getId(), user.getId()))
                    completionAssignment++;
                else
                    remainingAssignment++;
            }
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .studentNumber(user.getStudentNumber())
                .completionAssignment(completionAssignment)
                .remainingAssignment(remainingAssignment)
                .build();
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");

        for (int i = 0; i < 6; i++) {
            result.append(codes[new Random().nextInt(codes.length)]);
        }

        return result.toString();
    }

}
