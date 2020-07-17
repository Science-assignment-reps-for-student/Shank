package kr.hs.dsm_scarfs.shank.entites.file.sigle.repository;

import kr.hs.dsm_scarfs.shank.entites.file.sigle.SingleFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingleFileRepository extends CrudRepository<SingleFile, Integer> {
    boolean existsByHomeworkIdAndUserId(Integer homeWorkId, Integer userId);
}
