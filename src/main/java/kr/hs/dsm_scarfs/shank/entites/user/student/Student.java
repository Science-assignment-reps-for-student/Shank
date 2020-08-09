package kr.hs.dsm_scarfs.shank.entites.user.student;

import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String studentNumber;

    private String password;

    private String name;

    public String getStudentClassNumber() {
        return String.valueOf(this.studentNumber.charAt(1));
    }

    public kr.hs.dsm_scarfs.shank.entites.message.enums.AuthorityType getType() {
        return AuthorityType.STUDENT;
    }

}