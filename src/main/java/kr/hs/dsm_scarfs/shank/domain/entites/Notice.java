package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@Builder
@Entity(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;
}
