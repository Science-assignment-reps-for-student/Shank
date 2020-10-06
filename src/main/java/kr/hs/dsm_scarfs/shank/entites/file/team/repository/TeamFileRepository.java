package kr.hs.dsm_scarfs.shank.entites.file.team.repository;

import kr.hs.dsm_scarfs.shank.entites.file.team.TeamFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamFileRepository extends CrudRepository<TeamFile, Integer> {
    boolean existsByAssignmentIdAndTeamId(Integer assignmentId, Integer teamId);
}
