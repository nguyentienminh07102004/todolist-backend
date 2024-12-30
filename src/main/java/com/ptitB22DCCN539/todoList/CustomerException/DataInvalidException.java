package com.ptitB22DCCN539.todoList.CustomerException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataInvalidException extends RuntimeException {
    private ExceptionVariable exceptionVariable;
}