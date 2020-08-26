package kr.hs.dsm_scarfs.shank.entites.assignment;

import kr.hs.dsm_scarfs.shank.entites.assignment.enums.HomeworkType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {

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

    private String description;

    @Enumerated(EnumType.STRING)
    private HomeworkType type;

    private LocalDateTime createdAt;

    private Integer view;

    public Assignment view() {
        this.view++;
        return this;
    }

}