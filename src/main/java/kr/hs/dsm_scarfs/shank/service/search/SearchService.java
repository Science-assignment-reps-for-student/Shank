package kr.hs.dsm_scarfs.shank.service.search;

import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    ApplicationListResponse searchApplication(String query, Pageable page);
}
