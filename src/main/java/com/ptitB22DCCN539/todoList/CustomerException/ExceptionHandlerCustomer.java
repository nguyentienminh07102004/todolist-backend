package com.ptitB22DCCN539.todoList.CustomerException;

import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class ExceptionHandlerCustomer {
    @ExceptionHandler(value = DataInvalidException.class)
    public ResponseEntity<APIResponse> DataInvalidExceptionHandler(DataInvalidException e) {
        ExceptionVariable exceptionVariable = e.getExceptionVariable();
        APIResponse response = APIResponse.builder()
                .code(exceptionVariable.getCode())
                .message(exceptionVariable.getMessage())
                .build();
        return ResponseEntity.status(exceptionVariable.getStatus()).body(response);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<APIResponse> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        APIResponse response = APIResponse.builder()
                .response(exception.getMessage())
                .code(ExceptionVariable.DATA_INVALID.getCode())
                .message(ExceptionVariable.DATA_INVALID.getMessage())
                .build();
        return ResponseEntity.status(ExceptionVariable.DATA_INVALID.getStatus()).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        String message = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        APIResponse response = APIResponse.builder()
                .code(ExceptionVariable.DATA_INVALID.getCode())
                .message(message)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
