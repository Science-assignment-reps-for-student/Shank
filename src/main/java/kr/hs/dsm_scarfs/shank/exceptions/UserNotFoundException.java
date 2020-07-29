package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

}
