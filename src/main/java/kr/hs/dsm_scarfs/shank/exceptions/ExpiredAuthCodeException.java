package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class ExpiredAuthCodeException extends BusinessException {
    public ExpiredAuthCodeException() {
        super(ErrorCode.EXPIRED_AUTH_CODE);
    }

}
