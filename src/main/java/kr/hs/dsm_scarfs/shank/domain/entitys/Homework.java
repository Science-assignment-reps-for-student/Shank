package kr.hs.dsm_scarfs.shank.domain.entitys;

import com.sun.istack.NotNull;
import kr.hs.dsm_scarfs.shank.domain.entitys.homework.enums.HomeworkType;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;



@Setter
@Getter
@Builder
@Entity(name = "homework")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Date deadline_1;

    @NotNull
    private Date deadline_2;

    @NotNull
    private Date deadline_3;

    @NotNull
    private Date deadline_4;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private HomeworkType type;

    @NotNull
    private LocalDateTime createdAt;
}
