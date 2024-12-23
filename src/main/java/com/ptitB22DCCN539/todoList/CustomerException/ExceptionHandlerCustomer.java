package com.ptitB22DCCN539.todoList.CustomerException;

import com.ptitB22DCCN539.todoList.Modal.Response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerCustomer {
    @ExceptionHandler(value = DataInvalidException.class)
    public ResponseEntity<APIResponse> DataInvalidExceptionHandler(DataInvalidException e) {
        APIResponse response = APIResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
