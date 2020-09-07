package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;
import kr.hs.dsm_scarfs.shank.payload.response.MemberSearchResponse;

import java.util.List;

public interface MemberService {
    void setMember(MemberRequest memberRequest);
    void deleteMember(Integer targetId);
    List<MemberSearchResponse> searchMember(String query);
}
