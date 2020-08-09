package kr.hs.dsm_scarfs.shank.entites.message.repository;

import kr.hs.dsm_scarfs.shank.entites.message.Message;
import kr.hs.dsm_scarfs.shank.entites.message.enums.AuthorityType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<Message> findFirstByStudentIdOrderByTimeDesc(Integer userId);
    List<Message> findByUserIdOrderByMessageTimeAsc(Integer userId);
    List<Message> findAllByUserIdAndTargetIdAndTypeOrderByTimeAsc(Integer userId, Integer targetId, AuthorityType type);

}
