package kr.hs.dsm_scarfs.shank.service.homework;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkContentResponse;
import org.springframework.data.domain.Pageable;

public interface HomeworkService {
    ApplicationListResponse getHomeworkList(Pageable page);
    HomeworkContentResponse getHomeworkContent(Integer homeworkId);
}
