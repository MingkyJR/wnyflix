package com.wny.wnyflix.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {


        log.info("=================== START ===================");
        String requestURI = request.getRequestURI();
//        String traceId = UUID.randomUUID().toString();
//        MDC.put("trace.id", traceId);
//        log.info("trace.id : {}", traceId);
        log.info("[loginterceptor] requestURI : {}", requestURI);

        return true;  // false -> 이후에 진행을 하지 않는다.
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        log.info("MDC GET : {}", MDC.get("trace.id"));
//        MDC.clear();
        log.info("[loginterceptor] postHandle");
        log.info("=================== END ===================");

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("[loginterceptor] afterCompletion");
    }
}
