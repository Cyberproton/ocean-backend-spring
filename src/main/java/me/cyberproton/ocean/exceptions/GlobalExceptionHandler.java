package me.cyberproton.ocean.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.config.AppEnvironment;
import me.cyberproton.ocean.config.ExternalAppConfig;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@AllArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ExternalAppConfig appConfig;

    private boolean isProductionEnvironment() {
        return appConfig.env() == AppEnvironment.PRODUCTION;
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDatabaseException(DataAccessException ex, HttpServletRequest request) {
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(isProductionEnvironment() ? "Internal server error" : ex.getMessage())
                .path(request.getRequestURI())
                .build();
        Map<String, Object> res = Map.of(
                "error", error
        );
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, HttpServletRequest request) {
        // Log the exception
        logger.error("An exception occurred", ex);

        String message = ex.getMessage();
        ErrorMessage error = ErrorMessage.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(isProductionEnvironment() ? "Internal server error" : message)
                .path(request.getRequestURI())
                .build();
        Map<String, Object> res = Map.of(
                "error", error
        );
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }
}
