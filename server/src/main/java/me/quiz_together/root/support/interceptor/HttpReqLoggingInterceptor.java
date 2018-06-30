package me.quiz_together.root.support.interceptor;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;
import me.quiz_together.root.support.HttpServletRequestDumpUtils;
import me.quiz_together.root.support.security.SecurityUtils;

@Slf4j
@Component
public class HttpReqLoggingInterceptor extends HandlerInterceptorAdapter {

    private final static String LOG_FORMAT = "%s|%s|%s|%s|%s|%s|%s|%s";
    private final static String SUCC = "SUCC";
    private final static String FAIL = "FAIL";

    private ThreadLocal<Long> startTime = new ThreadLocal<>();
    private ThreadLocal<String> requestId = new ThreadLocal<>();

    /* Sucess or Failure, Status Code, Request Id, Request End Time, API, API Param, Response Data, Lang, Region, Client IP, Processing Time */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        requestId.set(request.getRemoteAddr() + ":" + System.nanoTime());

        MDC.put("tid", generateMDCValue());

        HttpServletRequestDumpUtils.printHttpServletRequestValue(request);

        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (response == null || request == null) {
            return;
        }

        Object result = request.getAttribute("BODY_LOG");

        String success = SUCC;
        int statusCode = response.getStatus();
        if (statusCode != 200) {
            success = FAIL;
        }

        LocalDateTime requestEndTime = LocalDateTime.now();
        Long requestIntervalTime = System.currentTimeMillis() - startTime.get();

        String clientIp = request.getRemoteAddr();
        String api = request.getRequestURI();

        String lang = Optional.ofNullable(request.getLocale()).map(Locale::getLanguage).orElse("U");
        String region = Optional.ofNullable(request.getLocale()).map(Locale::getCountry).orElse("U");

        String logStr = String.format(LOG_FORMAT, success, statusCode , requestEndTime, api, "lang-" + lang, region, clientIp, requestIntervalTime + "ms");

        log.info(logStr);
    }

    private String generateMDCValue() {
        long currentTimeMillis = System.currentTimeMillis();

        return String.format("%s", SecurityUtils.encryptSHA256(Long.toString(currentTimeMillis)).substring(0,10));
    }

}
