package kr.hs.dsm_scarfs.shank.entites.board;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private Integer view;

    public Board view() {
        this.view++;
        return this;
    }

    public Board update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

}
