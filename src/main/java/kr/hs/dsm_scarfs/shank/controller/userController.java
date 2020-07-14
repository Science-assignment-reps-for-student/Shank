package kr.hs.dsm_scarfs.shank.controller;

import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.service.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class userController {

    private final UserServiceImpl service;

    @PostMapping
    public void signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        service.signUp(signUpRequest);

    }

}
