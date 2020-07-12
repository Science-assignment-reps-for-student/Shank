package kr.hs.dsm_scarfs.shank.domain.entites;

import kr.hs.dsm_scarfs.shank.domain.entites.homework.enums.HomeworkType;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "homework")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date deadline_1;

    private Date deadline_2;

    private Date deadline_3;

    private Date deadline_4;

    private String title;

    private String description;

    private HomeworkType type;

    private LocalDateTime createdAt;
}
