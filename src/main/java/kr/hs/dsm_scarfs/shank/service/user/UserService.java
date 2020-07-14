package kr.hs.dsm_scarfs.shank.service.user;

import kr.hs.dsm_scarfs.shank.payload.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);

}
