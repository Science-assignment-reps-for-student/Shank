package kr.hs.dsm_scarfs.shank.entites.assignment;

import kr.hs.dsm_scarfs.shank.entites.assignment.enums.AssignmentType;
import kr.hs.dsm_scarfs.shank.exceptions.InvalidClassNumberException;
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
    private AssignmentType type;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "int default 0")
    private Integer view;

    public Assignment view() {
        this.view++;
        return this;
    }

    public LocalDate getCurrentDeadLine(Integer classNumber) {
        switch (classNumber) {
            case 1: return deadline1;
            case 2: return deadline2;
            case 3: return deadline3;
            case 4: return deadline4;
            default: throw new InvalidClassNumberException();
        }
    }

}