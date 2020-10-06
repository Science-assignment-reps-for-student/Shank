package kr.hs.dsm_scarfs.shank.security.auth;

import kr.hs.dsm_scarfs.shank.security.AuthorityType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUserEmail() {
        return this.getAuthentication().getName();
    }

    public AuthorityType getAuthorityType() {
        return ((AuthDetails) this.getAuthentication().getPrincipal()).getAuthorityType();
    }

}
