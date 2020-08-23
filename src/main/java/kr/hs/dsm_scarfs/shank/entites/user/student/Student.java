package kr.hs.dsm_scarfs.shank.entites.user.student;

import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Student implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;

    @Column(unique = true, nullable = false)
    private String studentNumber;

    public String getStudentClassNumber() {
        return String.valueOf(this.studentNumber.charAt(1));
    }

    public AuthorityType getType() {
        return AuthorityType.STUDENT;
    }

}