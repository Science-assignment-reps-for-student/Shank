package kr.hs.dsm_scarfs.shank.entites.user.admin.repository;

import kr.hs.dsm_scarfs.shank.entites.user.admin.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);

    @Query(value = "select a.* from admin as a inner join message as m on m.admin_id=a.id where m.student_id=?1 group by a.id", nativeQuery = true)
    List<Admin> getChattedAdmin(Integer studentId);
}
