package kr.hs.dsm_scarfs.shank.entites.notice.repository;

import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Integer> {
    Page<Notice> findAllBy(Pageable page);
}
