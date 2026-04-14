package com.employeemanagement.employee.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.employeemanagement.employee.dto.cloudinary.CloudinaryUploadResult;
import com.employeemanagement.employee.exception.AppException;
import com.employeemanagement.employee.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
	private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // Giới hạn kích thước avatar 5MB.

	Cloudinary cloudinary;

	/**
	 * Upload file ảnh lên cloudinary với folder chỉ định.
	 */
	public CloudinaryUploadResult uploadImage(MultipartFile file, String folder, String employeeId) {
		if(file == null || file.isEmpty()) {
			throw new AppException(ErrorCode.FILE_EMPTY);
		}
		if(file.getSize() > MAX_IMAGE_SIZE) {
			throw new AppException(ErrorCode.FILE_TOO_LARGE);
		}

		try{
			Map<String,Object> uploadResult = cloudinary.uploader().upload(
					file.getBytes(),
					ObjectUtils.asMap(
							"folder", folder,
							"resource_type", "image",
							"overwrite", true,
							"public_id", employeeId
					)
			);
			return CloudinaryUploadResult.builder()
					.publicId((String)uploadResult.get("public_id"))
					.url((String)uploadResult.get("url"))
					.secureUrl((String)uploadResult.get("secure_url"))
					.format((String)uploadResult.get("format"))
					.bytes(((Number) uploadResult.getOrDefault("bytes", 0L)).longValue())
					.width(((Number) uploadResult.getOrDefault("width", 0L)).intValue())
					.height(((Number) uploadResult.getOrDefault("height", 0L)).intValue())
					.build();
		}catch(IOException e){
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Xóa file trên Cloudinary dựa vào public_id.
	 */
	public void deleteImage(String publicId) {
		if(publicId == null || publicId.isBlank()) {
			return;
		}

		try{
			cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
		}catch(IOException e){
			throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}

/**
 * cloudinary.uploader().upload() nhận:
 *      + file.getBytes() – dữ liệu binary.
 *      + folder – thư mục: employees/<id>.
 *      + public_id – unique theo UUID.
 *      + overwrite = true – ghi đè nếu trùng public_id.
 * Upload xong Cloudinary trả về map → convert sang DTO.
 * deleteImage(): xoá file cũ nếu tồn tại public_id.
 */