package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class UserAlreadyIncludeException extends BusinessException {
    public UserAlreadyIncludeException() {
        super(ErrorCode.USER_ALREADY_INCLUDE_EXCEPTION);
    }
}
