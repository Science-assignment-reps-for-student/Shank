package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@Entity(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String name;
}
