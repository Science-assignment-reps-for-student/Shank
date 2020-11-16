package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;

public interface MemberService {
    void addMember(MemberRequest memberRequest);
    void deleteMember(Integer targetId);
}
