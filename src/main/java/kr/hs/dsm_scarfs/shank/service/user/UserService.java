package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;
import kr.hs.dsm_scarfs.shank.payload.request.VerifyCodeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.UserResponse;
import kr.hs.dsm_scarfs.shank.payload.response.UserSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
    void sendEmail(String email);
    void verifyEmail(VerifyCodeRequest verifyCodeRequest);
    UserResponse getUser(Pageable page);
    List<UserSearchResponse> searchUsers(String query);
}
