package com.employeemanagement.employee.dto.cloudinary;

import lombok.Builder;
import lombok.Value;

@Value // immutable class (lớp bất biến)
@Builder
public class CloudinaryUploadResult {
	String publicId; // public_id duy nhất của file trên Cloudinary.
	String url; // URL HTTP.

	// URL HTTPS (ưu tiên trả cho FE).
	String secureUrl; // là URL HTTPS → luôn dùng khi trả cho FE.
	String format;
	long bytes;
	int width;
	int height;
}
