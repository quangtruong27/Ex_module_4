package com.employeemanagement.employee.controller;


import com.employeemanagement.employee.dto.ApiResponse;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.model.Employee;
import com.employeemanagement.employee.model.Gender;
import com.employeemanagement.employee.util.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final List<Employee> employees = new ArrayList<>(
			Arrays.asList(
					new Employee(1, "Hoàng Văn Hải", LocalDate.of(1990, 1, 15), Gender.MALE, 15000000.00, "0975123542"),
					new Employee(2, "Trần Thị Hoài", LocalDate.of(1985, 5, 20), Gender.FEMALE, 14500000.00, "0967869868"),
					new Employee(3, "Lê Văn Sỹ", LocalDate.of(1992, 3, 10), Gender.MALE, 15500000.00, "0988881110"),
					new Employee(4, "Phạm Duy Khánh", LocalDate.of(1988, 7, 5), Gender.FEMALE, 14800000.00, "0986555333"),
					new Employee(5, "Hoàng Văn Quý", LocalDate.of(1995, 9, 25), Gender.MALE, 15200000.00, "0973388668")
			)
	);


	@GetMapping
	public ResponseEntity<ApiResponse<List<Employee>>> getAll() {
		return JsonResponse.ok(employees);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> getById(@PathVariable Integer id) {
		for(Employee employee : employees) {
			if(employee.getId() == id){
				return JsonResponse.ok(employee);
			}
		}
		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Employee>> create(@RequestBody Employee employee) {
		employee.setId((int) (Math.random() * 100000000));
		employees.add(employee);
		return JsonResponse.create(employee);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> update(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
		for (Employee employee : employees) {
			if (employee.getId().equals(id)) {
				employee.setName(updatedEmployee.getName());
				employee.setDob(updatedEmployee.getDob());
				employee.setGender(updatedEmployee.getGender());
				employee.setSalary(updatedEmployee.getSalary());
				employee.setPhone(updatedEmployee.getPhone());

				return JsonResponse.ok(employee);
			}
		}
		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
		for (Employee employee : employees) {
			if (employee.getId() == id) {
				employees.remove(employee);
				return JsonResponse.ok("Employee delete success!");
			}
		}
		throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
	}
}