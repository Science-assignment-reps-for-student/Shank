package kr.hs.dsm_scarfs.shank.entites.file.sigle;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
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