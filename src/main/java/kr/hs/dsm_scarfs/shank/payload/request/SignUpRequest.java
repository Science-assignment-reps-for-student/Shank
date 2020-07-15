package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class SignUpRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String number;

    @NotBlank
    private String name;

    @NotBlank
    private String authCode;

}
