package com.employeemanagement.employee.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
	EMPLOYEE_NOT_FOUND(40001,"Employee not found!", HttpStatus.NOT_FOUND),
	;
	int code;
	String message;
	HttpStatus status;
}
