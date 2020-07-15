package kr.hs.dsm_scarfs.shank.entites.authcode.repository;

import kr.hs.dsm_scarfs.shank.entites.authcode.AuthCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthCodeRepository extends CrudRepository<AuthCode, String> {
    Optional<AuthCode> findByStudentNumberAndCode(String number, String authCode);
}
