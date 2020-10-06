package kr.hs.dsm_scarfs.shank.service.member;

import kr.hs.dsm_scarfs.shank.payload.request.MemberRequest;

public interface MemberService {
    void setMember(MemberRequest memberRequest);
    void deleteMember(Integer targetId);
}
