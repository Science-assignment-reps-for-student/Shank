package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException(){
        super(ErrorCode.MEMBER_NOT_FOUND);
    }

}
