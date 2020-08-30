package kr.hs.dsm_scarfs.shank.entites.comment;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer authorId;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorType;

    private Integer boardId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Comment updateContent(String content) {
        this.content = content;
        this.updateAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        return this;
    }

}
