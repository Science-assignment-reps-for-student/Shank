package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationStatus;
import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.service.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final EmailVerificationRepository verificationRepository;
    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        studentRepository.findByStudentNumber(signUpRequest.getNumber()).ifPresent(student -> {
            throw new RuntimeException();
        });

       verificationRepository.findById(signUpRequest.getEmail())
               .filter(EmailVerification::isVerified)
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
                    .authCode(code)
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

        if (!emailVerification.getAuthCode().equals(code))
            throw new RuntimeException();

        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }


    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");

        for (int i = 0; i < 6; i++) {
            result.append(codes[(int) (Math.random() % codes.length)]);
        }
        return result.toString();
    }

}
