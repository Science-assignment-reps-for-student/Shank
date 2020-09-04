package kr.hs.dsm_scarfs.shank.service.notice;

import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import kr.hs.dsm_scarfs.shank.entites.notice.repository.NoticeRepository;

import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.exceptions.PermissionDeniedException;
import kr.hs.dsm_scarfs.shank.payload.request.NoticeRequest;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.NoticeResponse;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService, SearchService {

    private final NoticeRepository noticeRepository;

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;

    @Override
    public ApplicationListResponse getNoticeList(Pageable page) {
        return this.searchApplication("", page);
    }

    @Override
    public NoticeContentResponse getNoticeContent(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(ApplicationNotFoundException::new);

        Notice preNotice = noticeRepository.findTop1ByIdBeforeOrderByIdAsc(noticeId)
                .orElseGet(() -> Notice.builder().build());

        Notice nextNotice = noticeRepository.findTop1ByIdAfterOrderByIdAsc(noticeId)
                .orElseGet(() -> Notice.builder().build());


        noticeRepository.save(notice.view());

        return NoticeContentResponse.builder()
                    .title(notice.getTitle())
                    .content(notice.getContent())
                    .createdAt(notice.getCreatedAt())
                    .view(notice.getView())
                    .nextNoticeTitle(nextNotice.getTitle())
                    .preNoticeTitle(preNotice.getTitle())
                    .nextNoticeId(nextNotice.getId())
                    .preNoticeId(preNotice.getId())
                    .build();
    }

    @Override
    public Integer writeNotice(NoticeRequest noticeRequest) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        if (!user.getType().equals(AuthorityType.ADMIN))
            throw new PermissionDeniedException();

        Notice notice = noticeRepository.save(
                Notice.builder()
                    .title(noticeRequest.getTitle())
                    .content(noticeRequest.getContent())
                    .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                    .build()
        );

        return notice.getId();
    }

    @Override
    public ApplicationListResponse searchApplication(String query, Pageable page) {
        page = PageRequest.of(Math.max(0, page.getPageNumber()-1), page.getPageSize());
        Page<Notice> noticePage =
                noticeRepository.findAllByTitleContainsOrContentContainsOrderByCreatedAtDesc(query, query, page);

        List<NoticeResponse> noticeResponses = new ArrayList<>();

        for (Notice notice : noticePage) {
            String preViewContent = notice.getContent().substring(0, Math.min(50, notice.getContent().length()));
            noticeResponses.add(
                    NoticeResponse.builder()
                            .noticeId(notice.getId())
                            .view(notice.getView())
                            .title(notice.getTitle())
                            .createdAt(notice.getCreatedAt())
                            .preViewContent(preViewContent)
                            .build()
            );
        }


        return ApplicationListResponse.builder()
                .totalElements((int) noticePage.getTotalElements())
                .totalPages(noticePage.getTotalPages())
                .applicationResponses(noticeResponses)
                .build();
    }

}
