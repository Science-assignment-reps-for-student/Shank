package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "single_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SingleFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer homeworkId;

    private String fileName;

    @Column(unique = true, nullable = false)
    private String path;

    private LocalDateTime createdAt;

    private boolean isLate;
}
