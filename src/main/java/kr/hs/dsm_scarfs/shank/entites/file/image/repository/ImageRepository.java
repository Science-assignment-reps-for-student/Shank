package kr.hs.dsm_scarfs.shank.entites.file.image.repository;

import kr.hs.dsm_scarfs.shank.entites.file.image.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Integer> {
    void deleteByBoardId(Integer boardId);
    List<Image> findByBoardId(Integer boardId);
}
