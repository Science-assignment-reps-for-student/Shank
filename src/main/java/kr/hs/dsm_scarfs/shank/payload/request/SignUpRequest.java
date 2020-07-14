package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

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
