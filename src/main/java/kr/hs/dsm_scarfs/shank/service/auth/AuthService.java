package kr.hs.dsm_scarfs.shank.service.auth;

import kr.hs.dsm_scarfs.shank.payload.request.AccountRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(AccountRequest request);
    TokenResponse refreshToken(String refreshToken);
}
