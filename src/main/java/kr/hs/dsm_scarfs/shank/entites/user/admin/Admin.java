package kr.hs.dsm_scarfs.shank.entites.user.admin;

import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;

    public String getStudentNumber() {
        return "1100";
    }

    public Integer getStudentClassNumber() {
        return 1;
    }

    @Override
    public AuthorityType getType() {
        return AuthorityType.ADMIN;
    }

}