package kr.hs.dsm_scarfs.shank.entites.user;

import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import kr.hs.dsm_scarfs.shank.entites.user.admin.repository.AdminRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    public List<User> getChattedMessageList(User user) {
        List<User> users = new ArrayList<>();
        if (user.getType().equals(AuthorityType.STUDENT)) {
            users.addAll(adminRepository.getChattedAdmin(user.getId()));
        } else {
            users.addAll(studentRepository.getChattedStudent(user.getId()));
        }

        return users;
    }

    public int[] getSortedId(User user1, User user2) {
        if (user1.getType().equals(AuthorityType.STUDENT))
            return new int[]{user1.getId(), user2.getId()};
        else
            return new int[]{user2.getId(), user1.getId()};
    }

    public <T extends User> T getDefaultUser(Class<T> authorityType) {
        if (authorityType.equals(Student.class))
            return (T) Student.builder()
                    .studentNumber("1101")
                    .name("(알수없음)")
                    .build();
        else
            return (T) Admin.builder()
                    .name("(알수없음)")
                    .build();
    }

}
