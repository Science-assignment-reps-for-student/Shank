package kr.hs.dsm_scarfs.shank.domain.entitys;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity(name = "single_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SingleFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private Integer userId;

    @Column(unique = true)
    private Integer homeworkId;

    @NotNull
    private String fileName;

    @NotNull
    @Column(unique = true)
    private String path;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private boolean isLate;
}
