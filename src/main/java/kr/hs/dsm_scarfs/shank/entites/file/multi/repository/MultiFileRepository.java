package kr.hs.dsm_scarfs.shank.entites.file.multi.repository;

import kr.hs.dsm_scarfs.shank.entites.file.multi.MultiFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultiFileRepository extends CrudRepository<MultiFile, Integer> {
    boolean existsByHomeworkIdAndTeamId(Integer homeWorkId, Integer teamId);
}
