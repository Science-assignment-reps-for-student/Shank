package kr.hs.dsm_scarfs.shank.entites.member.repository;

import kr.hs.dsm_scarfs.shank.entites.member.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Integer> {
    Member findByStudentIdAndHomeworkId(Integer studentId, Integer homeworkId);
}
