package ro.pyc22.shop.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ro.pyc22.shop.model.HttpResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFound(UsernameNotFoundException ex){
        log.error("HANDLER HIT: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiError(ApiException exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(
                HttpResponse.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .message(exception.getMessage())
                        .build()
        );
    }


}



