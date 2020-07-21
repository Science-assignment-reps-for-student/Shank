package kr.hs.dsm_scarfs.shank.entites.comment;

import kr.hs.dsm_scarfs.shank.entites.comment.enums.CommentEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private CommentEnum authorType;

    private Integer boardId;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

}