package kr.hs.dsm_scarfs.shank.service.assignment;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.AssignmentContentResponse;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    ApplicationListResponse getAssignmentList(Pageable page);
    AssignmentContentResponse getAssignmentContent(Integer assignment);
}
