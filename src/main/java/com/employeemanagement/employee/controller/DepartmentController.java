package com.employeemanagement.employee.controller;

import com.employeemanagement.employee.dto.ApiResponse;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.model.Department;
import com.employeemanagement.employee.util.JsonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
	private final List<Department> departments = new ArrayList<>(
			Arrays.asList(
					new Department(1, "Quản Lý"),
					new Department(2, "Nhân Sự"),
					new Department(3, "Kế Toán"),
					new Department(4, "Sản Xuất"),
					new Department(5, "Sale")
			)
	);

	@GetMapping
	public ResponseEntity<List<Department>> getAllDepartments() {
		return ResponseEntity.ok(departments);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Department> getDepartmentById(@PathVariable Integer id) {
		for(Department department : departments) {
			if(department.getId() == id){
				return ResponseEntity.ok(department);
			}
		}
		throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody Department department) {
		department.setId((int)(Math.random() * 100000000));
		departments.add(department);
		return JsonResponse.create(department);
	}

	@PutMapping("{/id}")
	public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable Integer id, @RequestBody Department updateDepartment) {
		for (Department department : departments){
			if(department.getId().equals(id)){
				department.setName(updateDepartment.getName());

				return JsonResponse.ok(department);
			}
		}
		throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
	}

	@DeleteMapping("{/id}")
	public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable Integer id) {
		for (Department department : departments){
			if(department.getId() == id){
				departments.remove(department);
				return JsonResponse.ok("Department delete success!!");
			}
		}
		throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
	}
}
