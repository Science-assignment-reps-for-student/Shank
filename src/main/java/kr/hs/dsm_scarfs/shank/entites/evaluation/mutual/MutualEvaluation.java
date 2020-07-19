package kr.hs.dsm_scarfs.shank.entites.evaluation.mutual;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
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

    private LocalDateTime createdAt;

}