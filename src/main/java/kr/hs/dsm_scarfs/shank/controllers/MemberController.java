package kr.hs.dsm_scarfs.shank.controllers;

import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MemberSearchResponse;
import kr.hs.dsm_scarfs.shank.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public void setMember(@RequestBody MemberRequest memberRequest) {
        memberService.setMember(memberRequest);
    }

    @DeleteMapping("/{targetId}")
    public void deleteMember(@PathVariable Integer targetId) {
        memberService.deleteMember(targetId);
    }

    @GetMapping("/search")
    public List<MemberSearchResponse> searchMember(@RequestParam("query") String query) {
        return memberService.searchMember(query);
    }

}
