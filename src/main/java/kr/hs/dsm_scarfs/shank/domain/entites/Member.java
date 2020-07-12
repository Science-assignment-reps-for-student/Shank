package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@Entity(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer teamId;

    private Integer userId;
}
