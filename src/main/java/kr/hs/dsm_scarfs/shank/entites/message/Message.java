package kr.hs.dsm_scarfs.shank.entites.message;

import kr.hs.dsm_scarfs.shank.entites.message.enums.MessageType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer adminId;

    private String message;

    private LocalDateTime time;

    private MessageType type;

    private boolean isShow;

    private boolean isDeleted;

}
