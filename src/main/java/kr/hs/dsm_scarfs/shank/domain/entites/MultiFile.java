package kr.hs.dsm_scarfs.shank.domain.entites;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@Entity(name = "multi_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MultiFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer homeworkId;

    private Integer teamId;

    private String fileName;

    @Column(unique = true)
    private String path;

    private LocalDateTime createdAt;

    private boolean isLate;
}
