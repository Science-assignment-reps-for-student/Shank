package kr.hs.dsm_scarfs.shank.entites.homework;

import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate deadline_1;

    private LocalDate deadline_2;

    private LocalDate deadline_3;

    private LocalDate deadline_4;

    private String title;

    private String description;

    private HomeworkType type;

    private LocalDateTime createdAt;

}