package kr.hs.dsm_scarfs.shank.entites.team.repository;

import kr.hs.dsm_scarfs.shank.entites.team.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Integer> {
    Optional<Team> findByLeaderIdAndAssignmentId(Integer leaderId, Integer assignmentId);
}
