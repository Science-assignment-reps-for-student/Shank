package kr.hs.dsm_scarfs.shank.entites.user.admin;

import kr.hs.dsm_scarfs.shank.entites.user.User;
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
        return "1101";
    }

    public String getStudentClassNumber() {
        return "1";
    }

}