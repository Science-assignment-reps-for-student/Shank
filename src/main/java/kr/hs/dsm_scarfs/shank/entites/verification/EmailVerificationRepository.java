package kr.hs.dsm_scarfs.shank.entites.verification;


import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends KeyValueRepository<EmailVerification, String> {
}
