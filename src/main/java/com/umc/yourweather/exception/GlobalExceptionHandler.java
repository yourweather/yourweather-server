package com.umc.yourweather.exception;

import com.umc.yourweather.response.ResponseDto;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IllegalArgumentException.class,
            UserNotExistException.class,
            RuntimeException.class,
            WeatherNotFoundException.class,
            IllegalStateException.class,
            IOException.class
    })
    public ResponseDto<?> handler(Exception e) {
        return ResponseDto.fail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
