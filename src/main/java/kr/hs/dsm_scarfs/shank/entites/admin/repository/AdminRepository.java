package kr.hs.dsm_scarfs.shank.entites.admin.repository;

import kr.hs.dsm_scarfs.shank.entites.admin.Admin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
}
