package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class UserAlreadyEvaluationException extends BusinessException {
    public UserAlreadyEvaluationException() {
        super(ErrorCode.USER_ALREADY_EVALUATION_EXCEPTION);
    }
}
