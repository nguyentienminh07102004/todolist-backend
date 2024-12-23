package com.ptitB22DCCN539.todoList.CustomerException;

public class DataInvalidException extends RuntimeException {
    public DataInvalidException() {}
    public DataInvalidException(String message) {
        super(message);
    }
}
