package com.wny.wnyflix.interceptor;

import com.wny.wnyflix.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);



        if(session == null || session.getAttribute("AUTHUSER") == null) {

            // 로그인 되지 않음
            log.warn("[미인증 사용자 요청]");

            //로그인으로 redirect
            response.sendRedirect("/");
            return false;
        }
        User user = (User)session.getAttribute("AUTHUSER");
        log.info("[LoginCheckInterceptor] requestURI : {}, userId : {}", requestURI, user.getUserId());
        // 로그인 되어있을 떄
        return true;
    }
}
