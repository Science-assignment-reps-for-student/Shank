package kr.hs.dsm_scarfs.shank.service.homework;

import kr.hs.dsm_scarfs.shank.entites.file.multi.repository.MultiFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.sigle.repository.SingleFileRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.notice.Notice;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService, SearchService {

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;
    private final HomeworkRepository homeworkRepository;
    private final MemberRepository memberRepository;
    private final MultiFileRepository multiFileRepository;
    private final SingleFileRepository singleFileRepository;

    @SneakyThrows
    @Override
    public ApplicationListResponse getHomeworkList(Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        String methodName = "findAllByDeadline" + user.getStudentClassNumber() + "After";
        System.out.println(Arrays.toString(homeworkRepository.getClass().getDeclaredMethods()));
        return this.getHomeworkList(
                (Page<Homework>) homeworkRepository.getClass()
                    .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                    .invoke(homeworkRepository, page, LocalDate.MIN)
        );
    }

    @SneakyThrows
    @Override
    public HomeworkContentResponse getHomeworkContent(Integer homeworkId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(ApplicationNotFoundException::new);

        LocalDate deadLine = (LocalDate) Homework.class
                .getDeclaredMethod("getDeadline" + user.getStudentClassNumber())
                .invoke(homework);

        boolean isComplete = false;

        Homework nextHomework = homeworkRepository.findTop1ByIdAfterOrderByIdAsc(homeworkId)
                .orElseGet(() -> Homework.builder().build());

        Homework preHomework = homeworkRepository.findTop1ByIdBeforeOrderByIdAsc(homeworkId)
                .orElseGet(() -> Homework.builder().build());

        Optional<Member> member = memberRepository.findByStudentIdAndHomeworkId(user.getId(), homework.getId());
        if (homework.getType().equals(HomeworkType.MULTI)) {
            if (member.isPresent()) {
                if (multiFileRepository.existsByHomeworkIdAndTeamId(homework.getId(), member.get().getTeamId())) {
                    isComplete = true;
                }
            }
        } else {
            if (singleFileRepository.existsByHomeworkIdAndUserId(homework.getId(), user.getId()))
                isComplete = true;
        }

        homeworkRepository.save(homework.view());

        return HomeworkContentResponse.builder()
                    .title(homework.getTitle())
                    .type(homework.getType())
                    .createdAt(homework.getCreatedAt())
                    .deadLine(deadLine)
                    .view(homework.getView())
                    .content(homework.getContent())
                    .nextBoardTitle(nextHomework.getTitle())
                    .preBoardTitle(preHomework.getTitle())
                    .nextBoardId(nextHomework.getId())
                    .preBoardId(preHomework.getId())
                    .isComplete(isComplete)
                    .build();
    }

    @Override
    public ApplicationListResponse searchApplication(String query, Pageable page) {
        return this.getHomeworkList(
                homeworkRepository.findAllByTitleContainsOrContentContains(query, query, page)
        );
    }

    public ApplicationListResponse getHomeworkList(Page<Homework> homeworkPages) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        int totalElement = (int) homeworkPages.getTotalElements();
        int totalPage = homeworkPages.getTotalPages();

        for (Homework homework : homeworkPages) {
            boolean isComplete = false;
            if (homework.getType().equals(HomeworkType.MULTI)) {
                Optional<Member> member = memberRepository.findByStudentIdAndHomeworkId(user.getId(), homework.getId());
                if (member.isPresent()) {
                    isComplete = multiFileRepository.existsByHomeworkIdAndTeamId(homework.getId(),
                            member.get().getTeamId());
                }
            } else {
                isComplete = singleFileRepository.existsByHomeworkIdAndUserId(homework.getId(), user.getId());
            }

            homeworkResponses.add(
                    HomeworkResponse.builder()
                            .homeworkId(homework.getId())
                            .view(homework.getView())
                            .title(homework.getTitle())
                            .createdAt(homework.getCreatedAt())
                            .preViewContent(homework.getContent()
                                    .substring(0, Math.min(150, homework.getContent().length())))
                            .type(homework.getType())
                            .isComplete(isComplete)
                            .build()
            );
        }


        return ApplicationListResponse.builder()
                .totalElements(totalElement)
                .totalPages(totalPage)
                .applicationResponses(homeworkResponses)
                .build();
    }

}
