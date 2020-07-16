package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class AccountRequest {

    @Email
    private String email;

    @NotBlank
    private String password;
}
