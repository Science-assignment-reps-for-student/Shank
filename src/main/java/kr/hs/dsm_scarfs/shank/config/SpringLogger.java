package kr.hs.dsm_scarfs.shank.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringLogger implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        String stringDate= DateFor.format(date);
        String param = "";
        if (req.getQueryString() != null)
            param = "?" + req.getQueryString();

        try {
            chain.doFilter(request, response);

            System.out.printf(
                    "%s INFO - %s - [%s %s%s] %s%n",
                    stringDate,
                    req.getHeader("X-Real-IP"),
                    req.getMethod(),
                    req.getRequestURI(),
                    param,
                    res.getStatus()
            );

        } catch (Exception e) {
            System.out.printf(
                    "%s WARN - %s - [%s %s%s] %s : %s%n",
                    stringDate,
                    req.getHeader("X-Real-IP"),
                    req.getMethod(),
                    req.getRequestURI(),
                    param,
                    res.getStatus(),
                    e.getMessage()
            );
        }
    }

}