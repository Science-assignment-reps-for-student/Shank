package kr.hs.dsm_scarfs.shank.entites.board;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String content;

    private Integer classNumber;

    private Integer adminId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(columnDefinition = "integer default 0")
    private Integer view;

    public Board view() {
        this.view++;
        return this;
    }

    public Board update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return this;
    }

}
