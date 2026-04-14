package com.employeemanagement.employee.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {

	/**
	 * Lưu file xuống thư mục cấu hình và trả về đường dẫn tương đối (ví dụ: /images/2025/uuid.png).
	 *
	 * @param ownerId id của entity (ví dụ: employee) để phục vụ đặt tên file
	 * @param file multipart file cần lưu
	 * @return đường dẫn tương đối để lưu vào database
	 */
	String store(Integer ownerId, MultipartFile file);

	/**
	 * Xoá file theo đường dẫn tương đối (nếu tồn tại). Không throw lỗi nếu không tìm thấy file.
	 *
	 * @param relativePath ví dụ: /images/2025/uuid.png
	 */
	void delete(String relativePath);
}

/**
 * store(UUID ownerId, MultipartFile file): nhận ownerId (ví dụ employee.id) để đặt tên file theo owner, tránh trùng
 * và dễ quản lý; trả về đường dẫn tương đối để lưu vào DB (ví dụ /images/{ownerId}.png).
 * delete(String relativePath): given relative path, xóa file nếu có; không ném lỗi nặng nếu file không tồn tại.
 * ⇒ Mục đích interface: tách business logic khỏi chi tiết lưu trữ (dễ thay Local → S3 về sau).
 */