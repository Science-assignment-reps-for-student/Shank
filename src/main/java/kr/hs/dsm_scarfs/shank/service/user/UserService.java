package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.UserResponse;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
    void sendEmail(String email);
    void verifyEmail(VerifyCodeRequest verifyCodeRequest);
    UserResponse getUser();
}
