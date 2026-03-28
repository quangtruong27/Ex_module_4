package com.employeemanagement.employee.util;

import aQute.bnd.annotation.Resolution;
import com.employeemanagement.employee.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JsonResponse {
	public static <T>ResponseEntity<ApiResponse<T>> ok(T t){
		return ResponseEntity.ok(ApiResponse.<T>builder().data(t).build());
	}

	public static <T>ResponseEntity<ApiResponse<T>> create(T t){
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<T>builder().data(t).build());
	}

}
