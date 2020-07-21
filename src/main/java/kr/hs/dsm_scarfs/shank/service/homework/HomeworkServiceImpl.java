package kr.hs.dsm_scarfs.shank.service.homework;

import kr.hs.dsm_scarfs.shank.entites.file.multi.repository.MultiFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.sigle.repository.SingleFileRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.entites.user.student.Student;
import kr.hs.dsm_scarfs.shank.entites.user.student.repository.StudentRepository;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.HomeworkResponse;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService{

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;
    private final HomeworkRepository homeworkRepository;
    private final MemberRepository memberRepository;
    private final MultiFileRepository multiFileRepository;
    private final SingleFileRepository singleFileRepository;

    @SneakyThrows
    @Override
    public HomeworkListResponse getHomeworkList(Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        List<HomeworkResponse> homeworkResponses = new ArrayList<>();
        String methodName = "findAllByDeadline"+user.getStudentClassNumber()+"After";
        Page<Homework> homeworkPages = (Page<Homework>) HomeworkRepository.class
                .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                .invoke(homeworkRepository, page, LocalDate.MIN);

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


        return HomeworkListResponse.builder()
                .totalElements(totalElement)
                .totalPages(totalPage)
                .homeworkResponses(homeworkResponses)
                .build();
    }
}
