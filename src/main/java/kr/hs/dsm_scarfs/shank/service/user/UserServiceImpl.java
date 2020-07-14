package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerification;
import kr.hs.dsm_scarfs.shank.entites.verification.EmailVerificationRepository;
import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final StudentRepository studentRepository;
    private final EmailVerificationRepository verificationRepository;

    @Override
    public void signUp(SignUpRequest signUpRequest) {

        studentRepository.findByEmail(signUpRequest.getEmail()).ifPresent(student -> {
            throw new RuntimeException();
        });

        studentRepository.findByStudentNumber(signUpRequest.getNumber()).ifPresent(student -> {
            throw new RuntimeException();
        });

       verificationRepository.findById(signUpRequest.getEmail()).filter(EmailVerification::isVerified)
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

}
