package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class TargetNotFoundException extends BusinessException {
    public TargetNotFoundException() {
        super(ErrorCode.TARGET_NOT_FOUND);
    }

}
