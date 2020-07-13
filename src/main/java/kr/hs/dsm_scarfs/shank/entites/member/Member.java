package kr.hs.dsm_scarfs.shank.entites.member;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer teamId;

    private Integer userId;

    private Integer homeworkId;

}