package kr.hs.dsm_scarfs.shank.entites.comment.repository;

import kr.hs.dsm_scarfs.shank.entites.comment.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findAllByBoardIdOrderByIdAsc(Integer boardId);
}
