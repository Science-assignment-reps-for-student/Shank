package kr.hs.dsm_scarfs.shank.entites.comment.repository;

import kr.hs.dsm_scarfs.shank.entites.comment.Cocomment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocommentRepository extends CrudRepository<Cocomment, Integer> {
    List<Cocomment> findAllByCommentId(Integer commentId);
    void deleteAllByCommentId(Integer commentId);
}
