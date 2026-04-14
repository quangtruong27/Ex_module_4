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
	DEPARTMENT_NOT_EXISTED(40002,"Department is not existed!", HttpStatus.NOT_FOUND),
	FILE_EMPTY(40003,"File must not empty", HttpStatus.BAD_REQUEST),
	FILE_TOO_LARGE(40004,"File size must not exceed 5MB", HttpStatus.BAD_REQUEST),
	FILE_EXTENSION_MISSING(40005, "File extension is required.", HttpStatus.BAD_REQUEST),
	FILE_INVALID_TYPE(40006, "Invalid file format. Only image files are allowed.", HttpStatus.BAD_REQUEST),
	INTERNAL_SERVER_ERROR(50001, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
	FILE_UPLOAD_FAILED(50002, "Failed to store uploaded file.", HttpStatus.INTERNAL_SERVER_ERROR),

	;
	int code;
	String message;
	HttpStatus status;
}
