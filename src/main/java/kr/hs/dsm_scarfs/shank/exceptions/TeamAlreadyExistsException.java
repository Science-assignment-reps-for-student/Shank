package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class TeamAlreadyExistsException extends BusinessException {
    public TeamAlreadyExistsException() {
        super(ErrorCode.TEAM_ALREADY_EXISTS_EXCEPTION);
    }
}
