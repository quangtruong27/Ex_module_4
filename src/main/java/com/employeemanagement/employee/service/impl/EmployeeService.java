package com.employeemanagement.employee.service.impl;

import com.employeemanagement.employee.dto.cloudinary.CloudinaryUploadResult;
import com.employeemanagement.employee.dto.employee.EmployeeSearchRequest;
import com.employeemanagement.employee.entity.Employee;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import com.employeemanagement.employee.repository.IEmployeeRepository;
import com.employeemanagement.employee.service.CloudinaryService;
import com.employeemanagement.employee.service.FileStorageService;
import com.employeemanagement.employee.service.IEmployeeService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService implements IEmployeeService {

	FileStorageService fileStorageService;

	CloudinaryService cloudinaryService;
	private static final long MAX_AVATAR_SIZE = 5 * 1024 * 1024;

	IEmployeeRepository employeeRepository;

	@Override
	public Page<Employee> findByAttributes(EmployeeSearchRequest request, Pageable pageable) {
		return employeeRepository.findByAttributes(request, pageable);
	}

	@Override
	public Optional<Employee> findById(Integer id) {
		return employeeRepository.findById(id);
	}

	@Override
	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Override
	public Employee updateEmployee(Integer id, Employee updatedEmployee) {
		// Gán ID vào để JPA biết đây là "Cập nhật" chứ không phải "Thêm mới"
		updatedEmployee.setId(id);
		return employeeRepository.save(updatedEmployee);
	}

	@Override
	public void deleteEmployee(Integer id) {
		employeeRepository.deleteById(id);
	}

	@Override
	public Employee updateAvatarCloud(Integer Id, MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new AppException(ErrorCode.FILE_EMPTY);
		}

		if (file.getSize() > MAX_AVATAR_SIZE) {
			throw new AppException(ErrorCode.FILE_TOO_LARGE);
		}

		Employee employee = employeeRepository.findById(Id)
				.orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

		CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(file, "employee", "employee/" + Id);

		cloudinaryService.deleteImage(employee.getAvatarPublicId());

		employee.setAvatarUrl(uploadResult.getSecureUrl() != null
				&& !uploadResult.getSecureUrl().isEmpty() ? uploadResult.getSecureUrl() : uploadResult.getUrl());
		employee.setAvatarPublicId(uploadResult.getPublicId());

		return employeeRepository.save(employee);
	}

	@Override
	public Employee updateAvatar(Integer Id, MultipartFile file) {
		// 1. Kiểm tra multipart rỗng hoặc vượt giới hạn
		if (file == null || file.isEmpty()) {
			throw new AppException(ErrorCode.FILE_EMPTY);
		}
		if (file.getSize() > MAX_AVATAR_SIZE) {
			throw new AppException(ErrorCode.FILE_TOO_LARGE);
		}
		// 2. Tìm employee cần cập nhật
		Employee employee = employeeRepository.findById(Id)
				.orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

		try {
			// 3. Đọc toàn bộ byte[] và gán vào trường BLOB
			employee.setAvatarBlob(file.getBytes());
		} catch (IOException e) {
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		// 4. Lưu lại entity -> Hibernate ghi byte[] xuống cột avatar_blob
		return employeeRepository.save(employee);
	}

	@Override
	public Employee updateAvatarLS(Integer id, MultipartFile file) {
		// Đảm bảo employee tồn tại trước khi thao tác với file
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

		String oldAvatar = employee.getAvatar();
		// Lưu file mới, method store sẽ tự validate nội dung file
		String avatarPath = fileStorageService.store(id, file);
		if (oldAvatar != null && !oldAvatar.equals(avatarPath)) {
			fileStorageService.delete(oldAvatar);
		}
		employee.setAvatar(avatarPath);
		return employeeRepository.save(employee);
	}
}
