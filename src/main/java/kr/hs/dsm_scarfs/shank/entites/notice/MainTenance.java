package kr.hs.dsm_scarfs.shank.entites.notice;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MainTenance {

    @Id
    private Integer id;

    private LocalDateTime finishAt;

    private boolean ContentisTenance;

}
