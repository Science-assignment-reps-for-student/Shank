package kr.hs.dsm_scarfs.shank.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class ExceptionConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) {
        ExceptionHandlerFiler handlerFiler = new ExceptionHandlerFiler();
        http.addFilterBefore(handlerFiler, JwtTokenFilter.class);
    }
}
