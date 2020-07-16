package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.AccountRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TokenResponse;
import kr.hs.dsm_scarfs.shank.service.auth.AuthService;
import kr.hs.dsm_scarfs.shank.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public TokenResponse signIn(@RequestBody @Valid AccountRequest request) {
        System.out.println(request.getEmail());
        return authService.signIn(request);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

}
