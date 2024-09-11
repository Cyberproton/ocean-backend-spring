package me.cyberproton.ocean.exceptions;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.config.AppEnvironment;
import me.cyberproton.ocean.config.ExternalAppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private final ExternalAppConfig appConfig;
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private boolean isProductionEnvironment() {
        return appConfig.env() == AppEnvironment.PRODUCTION;
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDatabaseException(
            DataAccessException ex, HttpServletRequest request) {
        // Log the exception
        logger.error("An exception occurred", ex);

        ErrorMessage error =
                ErrorMessage.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(
                                isProductionEnvironment()
                                        ? "Internal server error"
                                        : ex.getMessage())
                        .path(request.getRequestURI())
                        .build();
        Map<String, Object> res = Map.of("error", error);
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(
            ResponseStatusException ex, HttpServletRequest request) {
        // Log the exception
        logger.error("An exception occurred", ex);
        ErrorMessage error =
                ErrorMessage.builder()
                        .status(ex.getStatusCode().value())
                        .message(ex.getReason())
                        .path(request.getRequestURI())
                        .build();
        Map<String, Object> res = Map.of("error", error);
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @Nonnull MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Log the exception
        logger.error("An exception occurred", ex);

        // Get first error message and return it
        // Check field errors first then global errors
        String message =
                ex.getBindingResult().getFieldErrors().stream()
                        .findFirst()
                        .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                        .orElseGet(
                                () ->
                                        ex.getBindingResult().getGlobalErrors().stream()
                                                .findFirst()
                                                .map(
                                                        DefaultMessageSourceResolvable
                                                                ::getDefaultMessage)
                                                .orElse("Bad request"));
        ErrorMessage error =
                ErrorMessage.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(appConfig.env().isProduction() ? "Bad request" : message)
                        .path(request.getRequestURI())
                        .build();
        Map<String, Object> res = Map.of("error", error);
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAnyException(Exception ex, HttpServletRequest request) {
        // Log the exception
        logger.error("An exception occurred", ex);

        String message = ex.getMessage();
        ErrorMessage error =
                ErrorMessage.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(isProductionEnvironment() ? "Internal server error" : message)
                        .path(request.getRequestURI())
                        .build();
        Map<String, Object> res = Map.of("error", error);
        return new ResponseEntity<>(res, HttpStatusCode.valueOf(error.getStatus()));
    }
}
