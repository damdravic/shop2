package ro.pyc22.shop.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiException extends RuntimeException{

    public ApiException(String message){

        super(message);
        log.error(message);
        log.info(message);
    }
}
