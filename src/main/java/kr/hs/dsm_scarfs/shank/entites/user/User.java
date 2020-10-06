package kr.hs.dsm_scarfs.shank.entites.user;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;

public interface User {
    Integer getId();
    String getEmail();
    String getPassword();
    String getName();
    String getStudentNumber();
    Integer getStudentClassNumber();
    AuthorityType getType();
}
