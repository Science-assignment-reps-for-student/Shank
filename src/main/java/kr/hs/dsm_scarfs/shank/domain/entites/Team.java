package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@Entity(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer leaderId;

    private Integer homeworkId;

    private String name;
}
