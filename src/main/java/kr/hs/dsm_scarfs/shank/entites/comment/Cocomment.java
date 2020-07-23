package kr.hs.dsm_scarfs.shank.entites.comment;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
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
public class Cocomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer authorId;

    private AuthorityType authorType;

    private Integer commentId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Cocomment updateContent(String content) {
        this.content = content;
        this.updateAt = LocalDateTime.now();
        return this;
    }


}
