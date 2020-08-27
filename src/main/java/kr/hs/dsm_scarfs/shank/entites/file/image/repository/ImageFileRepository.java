package kr.hs.dsm_scarfs.shank.entites.file.image.repository;

import kr.hs.dsm_scarfs.shank.entites.file.image.ImageFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFileRepository extends CrudRepository<ImageFile, Integer> {
    void deleteByBoardId(Integer boardId);
    List<ImageFile> findByBoardId(Integer boardId);
}
