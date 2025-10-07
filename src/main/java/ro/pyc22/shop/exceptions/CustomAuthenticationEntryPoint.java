package ro.pyc22.shop.exceptions;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ro.pyc22.shop.model.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
       log.warn("in AuthenticationEntryPoint");

         HttpResponse httpResponse = HttpResponse.builder()
                 .timeStamp(now().toString())
                 .reason("You need to log in to access this resource")
                 .httpStatus(UNAUTHORIZED)
                 .statusCode(UNAUTHORIZED.value())
                 .message("here my massage" + authException.getMessage())
                 .statusCode(UNAUTHORIZED.value())
                 .build();
         response.setContentType(APPLICATION_JSON_VALUE);
         response.setStatus(UNAUTHORIZED.value());

        OutputStream out =response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out,httpResponse);
        out.flush();


        }}

