package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class InvalidSearchTypeException extends BusinessException {
    public InvalidSearchTypeException() {
        super(ErrorCode.INVALID_SEARCH_TYPE);
    }
}
