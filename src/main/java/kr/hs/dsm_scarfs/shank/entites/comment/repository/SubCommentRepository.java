package kr.hs.dsm_scarfs.shank.entites.comment.repository;

import kr.hs.dsm_scarfs.shank.entites.comment.SubComment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCommentRepository extends CrudRepository<SubComment, Integer> {
    List<SubComment> findAllByCommentId(Integer commentId);
    void deleteAllByCommentId(Integer commentId);
}
