package kr.hs.dsm_scarfs.shank.service.homework;

import kr.hs.dsm_scarfs.shank.entites.file.multi.repository.MultiFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.sigle.repository.SingleFileRepository;
import kr.hs.dsm_scarfs.shank.entites.homework.Homework;
import kr.hs.dsm_scarfs.shank.entites.homework.enums.HomeworkType;
import kr.hs.dsm_scarfs.shank.entites.homework.repository.HomeworkRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.student.Student;
import kr.hs.dsm_scarfs.shank.entites.student.repository.StudentRepository;
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

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService{

    private final AuthenticationFacade authenticationFacade;

    private final StudentRepository studentRepository;
    private final HomeworkRepository homeworkRepository;
    private final MemberRepository memberRepository;
    private final MultiFileRepository multiFileRepository;
    private final SingleFileRepository singleFileRepository;

    @SneakyThrows
    @Override
    public HomeworkListResponse getHomeworkList(Pageable page) {
        Student student = studentRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(RuntimeException::new);

        List<HomeworkResponse> homeworkResponses = new ArrayList<>();
        String methodName = "findAllByDeadline"+student.getStudentClassNumber()+"After";
        Page<Homework> homeworkPages = (Page<Homework>) HomeworkRepository.class
                .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                .invoke(homeworkRepository, page, LocalDate.MIN);

        int totalElement = (int) homeworkPages.getTotalElements();
        int totalPage = homeworkPages.getTotalPages();

        for (Homework homework : homeworkPages) {
            boolean isComplete;
            if (homework.getType().equals(HomeworkType.MULTI)) {
                Member member = memberRepository.findByStudentIdAndHomeworkId(student.getId(), homework.getId());
                isComplete = multiFileRepository.existsByHomeworkIdAndTeamId(homework.getId(), member.getTeamId());
            } else {
                isComplete = singleFileRepository.existsByHomeworkIdAndUserId(homework.getId(), student.getId());
            }

            homeworkResponses.add(
                    HomeworkResponse.builder()
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
