package kr.hs.dsm_scarfs.shank.domain.entitys;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@Builder
@Entity(name = "self_evaluation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelfEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer homeworkId;

    private Integer scientificAccuracy;

    private Integer communication;

    private Integer attitude;
}
