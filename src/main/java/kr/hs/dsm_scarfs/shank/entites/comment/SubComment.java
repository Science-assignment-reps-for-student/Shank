package kr.hs.dsm_scarfs.shank.entites.comment;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity(name = "cocomment")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer authorId;

    @Enumerated(EnumType.STRING)
    private AuthorityType authorType;

    private Integer commentId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public SubComment updateContent(String content) {
        this.content = content;
        this.updateAt = LocalDateTime.now();
        return this;
    }


}
