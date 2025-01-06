package com.ptitB22DCCN539.todoList.CustomerException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionVariable {
    EMAIL_NOT_FOUND(404, "Email not found",HttpStatus.NOT_FOUND),
    PASSWORD_AND_REPEAT_PASSWORD_NOT_MATCH(400, "Password and repeat password is not match!", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_NOT_MATCH(400, "Old password is not match!", HttpStatus.BAD_REQUEST),
    OLD_PASSWORD_NEW_PASSWORD_MATCH(400, "Old password and new password mustn't match!", HttpStatus.BAD_REQUEST),
    EMAIL_OR_PASSWORD_NOT_MATCH(400, "Email or password is not match!", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(400, "Email already exists!", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE(400, "Account is inactive!", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOGIN_MAX_DEVICE(400, "Account is login max device!", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(400, "Role not found!", HttpStatus.BAD_REQUEST),
    LOGIN_FAILED(400, "Login failed!", HttpStatus.INTERNAL_SERVER_ERROR),
    CODE_INVALID(400, "Code is invalid!", HttpStatus.BAD_REQUEST),
    TASK_NOT_FOUND(400, "Task not found!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden!", HttpStatus.FORBIDDEN),
    TOKEN_INVALID(400, "Token is invalid!", HttpStatus.BAD_REQUEST),
    DATA_INVALID(400, "Data is invalid!", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Email is invalid!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password is invalid!", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(400, "Category not found!", HttpStatus.BAD_REQUEST),
    BOARD_NOT_FOUND(400, "Board not found!", HttpStatus.BAD_REQUEST),
    ;

    private final Integer code;
    private final String message;
    private final HttpStatus status;
}
