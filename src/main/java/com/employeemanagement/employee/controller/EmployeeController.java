package com.employeemanagement.employee.controller;


import com.employeemanagement.employee.dto.ApiResponse;
import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.entity.Employee;
import com.employeemanagement.employee.service.IEmployeeService;
import com.employeemanagement.employee.util.JsonResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/employees")
public class EmployeeController {

	IEmployeeService employeeService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Employee>>> getAll(EmployeeSearchRequest request) {

		List<Employee> employees = employeeService.findAll(request);

		return JsonResponse.ok(employees);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> getById(@PathVariable Integer id) {
		Employee employee = employeeService.findById(id).orElse(null);
		if (employee != null) {
			return JsonResponse.ok(employee);
		}
		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Employee>> create(@RequestBody Employee employee) {
		employeeService.createEmployee(employee);
		return JsonResponse.create(employee);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> update(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
		Employee employee = employeeService.updateEmployee(id, updatedEmployee);
		if(employee != null){
				return JsonResponse.ok(employee);
			}

		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
		Employee employee = employeeService.deleteEmployee(id);
		if(employee != null){
			return JsonResponse.ok("Employee delete success!");

		}
		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}
}