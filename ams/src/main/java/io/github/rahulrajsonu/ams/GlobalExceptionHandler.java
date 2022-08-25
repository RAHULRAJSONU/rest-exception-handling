package io.github.rahulrajsonu.ams;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${app.exception.stack-trace:true}")
    private boolean stackTraceEnabled;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException nfe) {
        HttpStatus status = HttpStatus.NOT_FOUND; // 404
        // converting the stack trace to String
        String stackTrace = getStackTrace(nfe);
        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        nfe.getMessage(),
                        stackTrace // specifying the stack trace in case of 500s
                ),
                status
        );
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpStatusCodeException.class, HttpServerErrorException.class})
    @ResponseBody
    public ResponseEntity<ErrorResponse> httpClientErrorException(HttpStatusCodeException e) throws IOException {
        ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(e.getRawStatusCode()).header("X-Backend-Status", String.valueOf(e.getRawStatusCode()));
        if (e.getResponseHeaders().getContentType() != null) {
            bodyBuilder.contentType(e.getResponseHeaders().getContentType());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponse.class);
        errorResponse.setStackTrace(getStackTrace(e));
        return bodyBuilder.body(errorResponse);
    }

    @ExceptionHandler(Exception.class) // exception handled
    public ResponseEntity<ErrorResponse> handleExceptions(
            Exception e
    ) {
        // ... potential custom logic

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500

        String stackTrace = getStackTrace(e);

        return new ResponseEntity<>(
                new ErrorResponse(
                        status,
                        e.getMessage(),
                        stackTrace // specifying the stack trace in case of 500s
                ),
                status
        );
    }

    private String getStackTrace(Exception e) {
        if(!stackTraceEnabled)
            return "Stack trace not enabled.";
        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stackTrace = stringWriter.toString();
        return stackTrace;
    }


}