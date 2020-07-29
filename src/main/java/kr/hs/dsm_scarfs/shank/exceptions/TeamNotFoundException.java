package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class TeamNotFoundException extends BusinessException {
    public TeamNotFoundException() {
        super(ErrorCode.TEAM_NOT_FOUND);
    }

}
