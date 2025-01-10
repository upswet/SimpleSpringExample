package ru.app.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Enumeration;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**Разрешаем CORS для сваггера*/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
        //registry.addMapping("/api-docs"); //только для сваггера
    }

    /**Добавим свой перехватчик*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggerInterceptor());
    }
}


//https://www.baeldung.com/spring-mvc-handlerinterceptor
/**Создадим собственный перехватчик для возможности логирования входных и выходных запросов и пропишем его*/
@Slf4j
class LoggerInterceptor implements HandlerInterceptor {
    @Override
    /**Как следует из названия, перехватчик вызывает preHandle() перед обработкой запроса.
     По умолчанию этот метод возвращает true, чтобы отправить запрос дальше, к методу обработчика. Однако мы можем указать Spring остановить выполнение, вернув false.
     Мы можем использовать хук для записи информации о параметрах запроса, например о том, откуда пришёл запрос.*/
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[preHandle][" + request + "]" + "[" + request.getMethod() + "]" + request.getRequestURI() + getParameters(request));
        return true;
    }

    @Override
    /**Мы можем использовать этот метод для получения данных запроса и ответа после отображения представления*/
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null){
            ex.printStackTrace();
        }

        log.info("[afterCompletion][" + request + "][exception: " + ex + "] => "+response);
    }


    private String getParameters(HttpServletRequest request) {
        StringBuffer posted = new StringBuffer();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr + "=");
            if (curr.contains("password")
                    || curr.contains("pass")
                    || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr!=null && !ipAddr.equals("")) {
            posted.append("&_psip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}