package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class NumberDuplicationException extends BusinessException {
    public NumberDuplicationException() {
        super(ErrorCode.NUMBER_DUPLICATION);
    }

}
