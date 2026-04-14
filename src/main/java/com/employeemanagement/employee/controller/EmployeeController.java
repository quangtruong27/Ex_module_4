package com.employeemanagement.employee.controller;

import com.employeemanagement.employee.dto.ApiResponse;
import com.employeemanagement.employee.dto.PageResponse;
import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.entity.Employee;
import com.employeemanagement.employee.service.IEmployeeService;
import com.employeemanagement.employee.util.JsonResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/employees")
public class EmployeeController {

	IEmployeeService employeeService;

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<Employee>>> findByAttributes(EmployeeSearchRequest request,
							@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(
				ApiResponse.<PageResponse<Employee>>builder()
				.data(new PageResponse<>(employeeService.findByAttributes(request, pageable)))
				.build()
		);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> getById(@PathVariable Integer id) {
		Employee employee = employeeService.findById(id).orElse(null);

		if (employee == null) {
			throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
		}
		return JsonResponse.ok(employee);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<Employee>> createEmployee(@RequestBody Employee employee) {
		Employee savedEmployee = employeeService.createEmployee(employee);
		return JsonResponse.create(savedEmployee);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Employee>> updateEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
		Employee existingEmployee = employeeService.findById(id).orElse(null);
		if(existingEmployee == null){
			throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
		}

		Employee employee = employeeService.updateEmployee(id, updatedEmployee);
		return JsonResponse.ok(employee);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Integer id) {
		Employee existingEmployee = employeeService.findById(id).orElse(null);
		if(existingEmployee == null){
			throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
		}

			employeeService.deleteEmployee(id);
		return JsonResponse.ok("Employee deleted successfully!");
	}

	//cloudinary
//	@PostMapping("/{id}/avatar")
//	public ResponseEntity<ApiResponse<Employee>> uploadImage(@PathVariable("id") Integer id,
//															 @RequestParam("file") MultipartFile file) {
//		return JsonResponse.ok(employeeService.updateAvatarCloud(id, file));
//	}

	//BLOB
//	@PostMapping("/{id}/avatar")
//	public ResponseEntity<ApiResponse<Employee>> uploadAvatar(@PathVariable("id") Integer id,
//															  @RequestParam("file") MultipartFile file) {
//		return JsonResponse.ok(employeeService.updateAvatar(id, file));
//	}

	// Local Server
	@PostMapping("/{id}/avatar")
	public ResponseEntity<?> uploadAvatar(@PathVariable("id") Integer id,
										  @RequestParam("file") MultipartFile file) {
		return JsonResponse.ok(employeeService.updateAvatarLS(id, file));
	}
}