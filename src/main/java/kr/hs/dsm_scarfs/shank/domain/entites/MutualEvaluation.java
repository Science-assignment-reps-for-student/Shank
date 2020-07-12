package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "mutual_evaluation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MutualEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer homeworkId;

    private Integer targetId;

    private Integer communication;

    private Integer cooperation;


}
