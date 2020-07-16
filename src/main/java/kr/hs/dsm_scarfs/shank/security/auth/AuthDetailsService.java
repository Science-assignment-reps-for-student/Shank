package kr.hs.dsm_scarfs.shank.security.auth;

import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public AuthDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return studentRepository.findByEmail(userEmail)
                .map(AuthDetails::new)
                .orElseThrow(RuntimeException::new);
    }

}
