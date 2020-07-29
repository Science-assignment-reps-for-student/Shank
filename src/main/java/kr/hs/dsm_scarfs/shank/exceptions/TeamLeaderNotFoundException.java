package kr.hs.dsm_scarfs.shank.exceptions;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;

public class TeamLeaderNotFoundException extends BusinessException {
    public TeamLeaderNotFoundException() {
        super(ErrorCode.TEAM_LEADER_NOT_FOUND);
    }
}
