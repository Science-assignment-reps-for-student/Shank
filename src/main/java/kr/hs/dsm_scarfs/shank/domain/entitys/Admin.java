package kr.hs.dsm_scarfs.shank.domain.entitys;

import com.sun.istack.NotNull;
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

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String name;
}
