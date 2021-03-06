package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.entites.authcode.repository.AuthCodeRepository;
import kr.hs.dsm_scarfs.shank.entites.file.experiment.repository.ExperimentFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.team.repository.TeamFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.personal.repository.PersonalFileRepository;
import kr.hs.dsm_scarfs.shank.entites.assignment.Assignment;
import kr.hs.dsm_scarfs.shank.entites.assignment.enums.AssignmentType;
import kr.hs.dsm_scarfs.shank.entites.assignment.repository.AssignmentRepository;
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
import kr.hs.dsm_scarfs.shank.payload.response.UserSearchResponse;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
    private final PersonalFileRepository personalFileRepository;
    private final ExperimentFileRepository experimentFileRepository;
    private final TeamFileRepository teamFileRepository;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
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
                   .password(passwordEncoder.encode(signUpRequest.getPassword()))
                   .name(signUpRequest.getName())
                   .build()
       );

       authCodeRepository.deleteById(signUpRequest.getNumber());
    }

    @Override
    public void sendEmail(String email) {
        studentRepository.findByEmail(email).ifPresent(student -> {
            throw new UserAlreadyExistsException();
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
                .orElseThrow(InvalidAuthEmailException::new);

        try {
            if (!emailVerification.getCode().equals(code))
                throw new InvalidAuthCodeException();
        } catch (NullPointerException e) {
            throw new InvalidAuthEmailException();
        }

        emailVerificationRepository.save(emailVerification.verify());
    }

    @SneakyThrows
    @Override
    public UserResponse getUser(Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        int remainingAssignment = 0, completionAssignment = 0;

        String methodName = "findAllByDeadline" + user.getStudentClassNumber() + "AfterOrderByCreatedAtDesc";
        Page<Assignment> assignmentPage = (Page<Assignment>) AssignmentRepository.class
                .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                .invoke(assignmentRepository, page, LocalDate.now(ZoneId.of("Asia/Seoul")));

        for (Assignment assignment : assignmentPage) {
            if (user.getType().equals(AuthorityType.ADMIN)) break;

            if (assignment.getType().equals(AssignmentType.TEAM)) {
                Optional<Member> member = memberRepository.findByStudentIdAndAssignmentId(user.getId(), assignment.getId());
                if (member.isPresent()) {
                    if (teamFileRepository.existsByAssignmentIdAndTeamId(assignment.getId(), member.get().getTeamId())) {
                        completionAssignment++;
                        continue;
                    }
                }

                remainingAssignment++;
            } else if (assignment.getType().equals(AssignmentType.PERSONAL)) {
                if (personalFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId()))
                    completionAssignment++;
                else
                    remainingAssignment++;
            } else {
                if (experimentFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId()))
                    completionAssignment++;
                else
                    remainingAssignment++;
            }
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .studentNumber(user.getStudentNumber())
                .type(user.getType())
                .completionAssignment(completionAssignment)
                .remainingAssignment(remainingAssignment)
                .build();
    }

    @Override
    public List<UserSearchResponse> searchUsers(String query, Integer assignmentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<UserSearchResponse> responses = new ArrayList<>();
        List<Student> students;
        if (user.getType().equals(AuthorityType.ADMIN))
            students = studentRepository.findAllByNameContainsOrStudentNumberContains(query, query);
        else
            students = studentRepository.searchMember(
                    assignmentId,
                    "%" + query + "%",
                    "_" + user.getStudentClassNumber() + "__"
            );

        for (Student student : students) {
            if (user.getType().equals(AuthorityType.STUDENT) && user.getId().equals(student.getId())) continue;

            responses.add(
                    UserSearchResponse.builder()
                            .id(student.getId())
                            .number(student.getStudentNumber())
                            .name(student.getName())
                            .build()
            );
        }

        return responses;
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
