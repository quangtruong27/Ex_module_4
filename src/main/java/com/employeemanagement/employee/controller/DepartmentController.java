package com.employeemanagement.employee.controller;

import com.employeemanagement.employee.dto.ApiResponse;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.entity.Department;
import com.employeemanagement.employee.service.IDepartmentService;
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
@RequestMapping("/departments")
public class DepartmentController {

	IDepartmentService departmentService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
		List<Department> departments = departmentService.findAllDepartments();
		// Đã sửa lại dùng JsonResponse cho đồng bộ form API
		return JsonResponse.ok(departments);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable Integer id) {
		Department department = departmentService.findDepartmentById(id);
		if(department == null){
			throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
		}
		// Đã sửa lại dùng JsonResponse
		return JsonResponse.ok(department);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Department>> createDepartment(@RequestBody Department department) {
		Department savedDepartment = departmentService.createDepartment(department);
		return JsonResponse.create(savedDepartment);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Department>> updateDepartment(@PathVariable Integer id, @RequestBody Department updateDepartment) {
		// 1. Kiểm tra tồn tại trước khi cập nhật
		Department existingDept = departmentService.findDepartmentById(id);
		if(existingDept == null){
			throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
		}

		// 2. Thực hiện cập nhật
		Department department = departmentService.updateDepartment(id, updateDepartment);
		return JsonResponse.ok(department);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteDepartment(@PathVariable Integer id) {
		// 1. Kiểm tra tồn tại trước khi xóa
		Department existingDept = departmentService.findDepartmentById(id);
		if(existingDept == null){
			throw new AppException(ErrorCode.DEPARTMENT_NOT_EXISTED);
		}

		// 2. Gọi hàm xóa (Hàm này chạy void, không gán biến)
		departmentService.deleteDepartment(id);
		return JsonResponse.ok("Department delete success!!");
	}
}