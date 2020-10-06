package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class InvalidClassNumberException extends BusinessException {
    public InvalidClassNumberException() {
        super(ErrorCode.INVALID_CLASS_NUMBER);
    }
}
