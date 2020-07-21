package kr.hs.dsm_scarfs.shank.entites.user;

import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;

    public AuthDetails getAuthDetails(String userEmail) {
        return studentRepository.findByEmail(userEmail)
                .map(student -> new AuthDetails(student, AuthorityType.STUDENT))
                .orElseGet(() -> adminRepository.findByEmail(userEmail)
                        .map(admin -> new AuthDetails(admin, AuthorityType.ADMIN))
                        .orElseThrow(RuntimeException::new)
                );
    }

    public User getUser(String email) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) return student.get();
        else return adminRepository.findByEmail(email)
                .orElseThrow(RuntimeException::new);
    }

}
