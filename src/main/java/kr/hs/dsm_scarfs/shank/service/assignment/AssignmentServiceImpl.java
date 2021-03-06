package kr.hs.dsm_scarfs.shank.service.assignment;

import kr.hs.dsm_scarfs.shank.entites.file.experiment.repository.ExperimentFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.team.repository.TeamFileRepository;
import kr.hs.dsm_scarfs.shank.entites.file.personal.repository.PersonalFileRepository;
import kr.hs.dsm_scarfs.shank.entites.assignment.Assignment;
import kr.hs.dsm_scarfs.shank.entites.assignment.enums.AssignmentType;
import kr.hs.dsm_scarfs.shank.entites.assignment.repository.AssignmentRepository;
import kr.hs.dsm_scarfs.shank.entites.member.Member;
import kr.hs.dsm_scarfs.shank.entites.member.repository.MemberRepository;
import kr.hs.dsm_scarfs.shank.entites.user.User;
import kr.hs.dsm_scarfs.shank.entites.user.UserFactory;
import kr.hs.dsm_scarfs.shank.exceptions.ApplicationNotFoundException;
import kr.hs.dsm_scarfs.shank.payload.response.ApplicationListResponse;
import kr.hs.dsm_scarfs.shank.payload.response.AssignmentContentResponse;
import kr.hs.dsm_scarfs.shank.payload.response.AssignmentResponse;
import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import kr.hs.dsm_scarfs.shank.security.auth.AuthenticationFacade;
import kr.hs.dsm_scarfs.shank.service.search.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AuthenticationFacade authenticationFacade;
    private final UserFactory userFactory;
    private final AssignmentRepository assignmentRepository;
    private final MemberRepository memberRepository;
    private final TeamFileRepository teamFileRepository;
    private final PersonalFileRepository personalFileRepository;
    private final ExperimentFileRepository experimentFileRepository;

    @SneakyThrows
    @Override
    public ApplicationListResponse getAssignmentList(Integer classNumber, Pageable page) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        page = PageRequest.of(Math.max(0, page.getPageNumber()-1), page.getPageSize());

        String methodName;
        if (user.getType().equals(AuthorityType.ADMIN) && classNumber != null)
            methodName = "findAllByDeadline" + classNumber + "AfterOrderByCreatedAtDesc";
        else
            methodName = "findAllByDeadline" + user.getStudentClassNumber() + "AfterOrderByCreatedAtDesc";

        return this.getAssignmentList(classNumber,
                (Page<Assignment>) assignmentRepository.getClass()
                    .getDeclaredMethod(methodName, Pageable.class, LocalDate.class)
                    .invoke(assignmentRepository, page, LocalDate.ofYearDay(2020, 1))
        );
    }

    @SneakyThrows
    @Override
    public AssignmentContentResponse getAssignmentContent(Integer assignmentId) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(ApplicationNotFoundException::new);

        LocalDate deadLine = assignment.getCurrentDeadLine(user.getStudentClassNumber());

        boolean isComplete = false;

        Assignment nextAssignment = assignmentRepository.findTop1ByIdAfterOrderByIdAsc(assignmentId)
                .orElseGet(() -> Assignment.builder().build());

        Assignment preAssignment = assignmentRepository.findTop1ByIdBeforeOrderByIdDesc(assignmentId)
                .orElseGet(() -> Assignment.builder().build());

        Optional<Member> member = memberRepository.findByStudentIdAndAssignmentId(user.getId(), assignment.getId());
        if (assignment.getType().equals(AssignmentType.TEAM)) {
            if (member.isPresent()) {
                if (teamFileRepository.existsByAssignmentIdAndTeamId(assignment.getId(), member.get().getTeamId())) {
                    isComplete = true;
                }
            }
        } else if (assignment.getType().equals(AssignmentType.PERSONAL)){
            if (personalFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId()))
                isComplete = true;
        } else {
            if (experimentFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId()))
                isComplete = true;
        }

        assignmentRepository.save(assignment.view());

        return AssignmentContentResponse.builder()
                    .title(assignment.getTitle())
                    .type(assignment.getType())
                    .createdAt(assignment.getCreatedAt())
                    .deadLine(deadLine)
                    .view(assignment.getView())
                    .description(assignment.getDescription())
                    .deadLine(assignment.getCurrentDeadLine(user.getStudentClassNumber()))
                    .nextBoardTitle(nextAssignment.getTitle())
                    .preBoardTitle(preAssignment.getTitle())
                    .nextBoardId(nextAssignment.getId())
                    .preBoardId(preAssignment.getId())
                    .isComplete(isComplete)
                    .build();
    }

    @Override
    public ApplicationListResponse searchAssignment(String query, Pageable page) {
        page = PageRequest.of(Math.max(0, page.getPageNumber()-1), page.getPageSize());
        return this.getAssignmentList(null,
                assignmentRepository.findAllByTitleContainsOrDescriptionContainsOrderByCreatedAtDesc(query, query, page)
        );
    }

    private ApplicationListResponse getAssignmentList(Integer classNumber, Page<Assignment> assignmentPages) {
        User user = userFactory.getUser(authenticationFacade.getUserEmail());
        if (user.getType().equals(AuthorityType.STUDENT))
            classNumber = user.getStudentClassNumber();

        List<AssignmentResponse> assignmentResponses = new ArrayList<>();

        int totalElement = (int) assignmentPages.getTotalElements();
        int totalPage = assignmentPages.getTotalPages();

        for (Assignment assignment : assignmentPages) {
            boolean isComplete = false;
            if (assignment.getType().equals(AssignmentType.TEAM)) {
                Optional<Member> member =
                        memberRepository.findByStudentIdAndAssignmentId(user.getId(), assignment.getId());
                if (member.isPresent()) {
                    isComplete = teamFileRepository.existsByAssignmentIdAndTeamId(assignment.getId(),
                            member.get().getTeamId());
                }
            } else if (assignment.getType().equals(AssignmentType.PERSONAL)){
                isComplete = personalFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId());
            } else {
                isComplete = experimentFileRepository.existsByAssignmentIdAndStudentId(assignment.getId(), user.getId());
            }

            String preViewDescription =
                    assignment.getDescription().substring(0, Math.min(50, assignment.getDescription().length()));
            assignmentResponses.add(
                    AssignmentResponse.builder()
                            .assignmentId(assignment.getId())
                            .view(assignment.getView())
                            .title(assignment.getTitle())
                            .createdAt(assignment.getCreatedAt())
                            .preViewDescription(preViewDescription)
                            .deadLine(assignment.getCurrentDeadLine(user.getStudentClassNumber()))
                            .type(assignment.getType())
                            .isComplete(isComplete)
                            .build()
            );
        }


        return ApplicationListResponse.builder()
                .totalElements(totalElement)
                .totalPages(totalPage)
                .classNumber(classNumber)
                .applicationResponses(assignmentResponses)
                .build();
    }

}
