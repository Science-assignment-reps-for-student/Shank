package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class MemberAlreadyIncludeException extends BusinessException {
    public MemberAlreadyIncludeException() {
        super(ErrorCode.MEMBER_ALREADY_INCLUDE_EXCEPTION);
    }
}
