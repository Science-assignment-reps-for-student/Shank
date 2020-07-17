package kr.hs.dsm_scarfs.shank.entites.member.repository;

import kr.hs.dsm_scarfs.shank.entites.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<Member, Integer> {
    Optional<Member> findByUserIdAndTeamIdIn(Integer id, List<Integer> teamId);
}
