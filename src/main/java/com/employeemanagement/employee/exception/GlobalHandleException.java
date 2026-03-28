package com.employeemanagement.employee.exception;

import com.employeemanagement.employee.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {
	@ExceptionHandler(AppException.class)
	public ResponseEntity<?> handleAppException(AppException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(
				ApiResponse.builder()
						.code(errorCode.getCode())
						.message(errorCode.getMessage())
						.build()
		);
	}
}