package kr.hs.dsm_scarfs.shank.security;

import kr.hs.dsm_scarfs.shank.error.exception.BusinessException;
import kr.hs.dsm_scarfs.shank.error.exception.ErrorCode;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandlerFiler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        }catch (ServletException | IOException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }

}
