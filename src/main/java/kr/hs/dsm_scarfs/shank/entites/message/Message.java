package kr.hs.dsm_scarfs.shank.entites.message;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer studentId;

    private Integer adminId;

    private String message;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private AuthorityType type;

    private boolean isShow;

    private boolean isDeleted;

    public Message read() {
        this.isShow = true;
        return this;
    }

}