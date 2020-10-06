package kr.hs.dsm_scarfs.shank.service.auth;

import kr.hs.dsm_scarfs.shank.entites.refresh_token.RefreshToken;
import kr.hs.dsm_scarfs.shank.entites.refresh_token.RefreshTokenRepository;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.exceptions.InvalidTokenException;
import kr.hs.dsm_scarfs.shank.exceptions.UserNotFoundException;
import kr.hs.dsm_scarfs.shank.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import kr.hs.dsm_scarfs.shank.payload.request.AccountRequest;
import kr.hs.dsm_scarfs.shank.payload.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshExp;

    @Value("${auth.jwt.prefix}")
    private String tokenType;

    private final StudentRepository studentRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenResponse signIn(AccountRequest request) {
        return studentRepository.findByEmail(request.getEmail())
                .filter(student -> passwordEncoder.matches(request.getPassword(), student.getPassword()))
                    .map(Student::getEmail)
                    .map(email -> {
                        String refreshToken = tokenProvider.generateRefreshToken(email);
                        return new RefreshToken(email, refreshToken, refreshExp);
                    })
                    .map(refreshTokenRepository::save)
                    .map(refreshToken -> {
                        String accessToken = tokenProvider.generateAccessToken(refreshToken.getEmail());
                        return new TokenResponse(accessToken, refreshToken.getRefreshToken(), tokenType);
                    })
                .orElseThrow(UserNotFoundException::new);

    }

    @Override
    public TokenResponse refreshToken(String receivedToken) {
        if (!tokenProvider.isRefreshToken(receivedToken))
            throw new InvalidTokenException();

        return refreshTokenRepository.findByRefreshToken(receivedToken)
                .map(refreshToken -> {
                    String generatedAccessToken = tokenProvider.generateRefreshToken(refreshToken.getEmail());
                    return refreshToken.update(generatedAccessToken, refreshExp);
                })
                .map(refreshTokenRepository::save)
                .map(refreshToken -> {
                    String generatedAccessToken = tokenProvider.generateAccessToken(refreshToken.getEmail());
                    return new TokenResponse(generatedAccessToken, refreshToken.getRefreshToken(), tokenType);
                })
                .orElseThrow(InvalidTokenException::new);
    }

}
