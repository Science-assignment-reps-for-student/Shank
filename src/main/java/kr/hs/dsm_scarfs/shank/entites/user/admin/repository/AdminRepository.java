package kr.hs.dsm_scarfs.shank.entites.user.admin.repository;

import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
}
