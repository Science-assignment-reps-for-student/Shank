package kr.hs.dsm_scarfs.shank.entites.verification;


import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends KeyValueRepository<EmailVerification, String> {
}
