package kr.hs.dsm_scarfs.shank.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_AUTH_CODE(400,"Invalid Auth Code"),
    EXPIRED_AUTH_CODE(400,"Expired Auth Code"),
    INVALID_AUTH_EMAIL(400,"Invalid Auth Email"),
    INVALID_CLASS_NUMBER(400,"Invalid Class Number"),
    INVALID_TOKEN(401,"Invalid Token"),
    EXPIRED_TOKEN(401,"Expired Token"),
    USER_NOT_LEADER(401, "User Not Leader"),
    PERMISSION_DENIED_EXCEPTION(401,"Permission Denied Exception"),
    TEAM_NOT_FOUND(404,"Team Not Found"),
    USER_NOT_FOUND(404,"User Not Found"),
    MEMBER_NOT_FOUND(404,"Member Not Found"),
    TARGET_NOT_FOUND(404,"Target Not Found"),
    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    MESSAGE_NOT_FOUND(404, "Message Not Found"),
    APPLICATION_NOT_FOUND(404,"Application Not Found"),
    TEAM_LEADER_NOT_FOUND(404, "Team Leader Not Found"),
    IMAGE_NOT_FOUND(404, "Image Not Found"),
    NUMBER_DUPLICATION(409,"Number Duplication"),
    USER_ALREADY_EVALUATION_EXCEPTION(409, "User Already Evaluation Exception"),
    USER_ALREADY_EXISTS_EXCEPTION(409,"User Already Exists Exception"),
    USER_ALREADY_INCLUDE_EXCEPTION(409,"User Already Include Exception"),
    MEMBER_ALREADY_INCLUDE_EXCEPTION(409, "Member Already Include Exception");

    private final int status;

    private final String message;

}
