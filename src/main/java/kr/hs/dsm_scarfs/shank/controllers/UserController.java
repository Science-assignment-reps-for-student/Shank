package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.UserResponse;
import kr.hs.dsm_scarfs.shank.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserResponse getUser(Pageable page) {
        return userService.getUser(page);
    }

    @PostMapping
    public void signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
    }

    @PostMapping("/email/verify")
    public void sendEmail(@RequestParam("email") @Email String email) {
        userService.sendEmail(email);
    }

    @PutMapping("/email/verify")
    public void verifyEmail(@RequestBody @Valid VerifyCodeRequest verifyCodeRequest) {
        userService.verifyEmail(verifyCodeRequest);
    }

}
