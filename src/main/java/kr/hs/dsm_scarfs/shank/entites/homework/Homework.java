package kr.hs.dsm_scarfs.shank.entites.homework;

import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deadline_1")
    private LocalDate deadline1;

    @Column(name = "deadline_2")
    private LocalDate deadline2;

    @Column(name = "deadline_3")
    private LocalDate deadline3;

    @Column(name = "deadline_4")
    private LocalDate deadline4;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private HomeworkType type;

    private LocalDateTime createdAt;

}