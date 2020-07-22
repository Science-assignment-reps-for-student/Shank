package kr.hs.dsm_scarfs.shank.service.homework;

import kr.hs.dsm_scarfs.shank.payload.response.HomeworkContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkListResponse;
import org.springframework.data.domain.Pageable;

public interface HomeworkService {
    HomeworkListResponse getHomeworkList(Pageable page);
    HomeworkContentResponse getHomeworkContent(Integer homeworkId);
}
