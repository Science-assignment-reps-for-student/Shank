package kr.hs.dsm_scarfs.shank.entites.file.personal;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer studentId;

    private Integer assignmentId;

    private String fileName;

    @Column(unique = true, nullable = false)
    private String path;

    private LocalDateTime createdAt;

    private boolean isLate;

}