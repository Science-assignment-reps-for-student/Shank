package kr.hs.dsm_scarfs.shank.entites.authcode;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {

    @Id
    private String studentNumber;

    private String code;

}
