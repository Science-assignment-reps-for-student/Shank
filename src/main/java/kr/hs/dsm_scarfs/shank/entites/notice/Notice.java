package kr.hs.dsm_scarfs.shank.entites.notice;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    @Column(columnDefinition = "integer default 0")
    private Integer view;

    public Notice view() {
        this.view++;
        return this;
    }

}