package kr.hs.dsm_scarfs.shank.entites.notice.repository;

import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Integer> {
    Page<Notice> findAllBy(Pageable page);
    Page<Notice> findAllByTitleContainsOrContentContains(String tileQuery, String contentQuery, Pageable page);

    Optional<Notice> findTop1ByIdBeforeOrderByIdAsc(Integer id);

    Optional<Notice> findTop1ByIdAfterOrderByIdAsc(Integer id);

}
