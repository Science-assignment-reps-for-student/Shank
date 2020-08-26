package kr.hs.dsm_scarfs.shank.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {

    private Integer id;

    private String message;

    private AuthorityType type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

}
