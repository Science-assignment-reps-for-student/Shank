package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class ApplicationNotFoundException extends BusinessException {
    public ApplicationNotFoundException() {
        super(ErrorCode.APPLICATION_NOT_FOUND);
    }

}
