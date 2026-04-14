package com.employeemanagement.employee.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration // đánh dấu đây là class cấu hình.
public class CloudinaryConfig {
	@Value("${cloudinary.cloud-name}") // Các biến @Value đọc giá trị từ application.properties.
	private String cloudName;

	@Value("${cloudinary.api-key}")
	private String apiKey;

	@Value("${cloudinary.api-secret}")
	private String apiSecret;

	@Bean
	public Cloudinary cloudinary() { // Phương thức cloudinary() trả về 1 bean Cloudinary dạng singleton.
		Map<String, String> config = ObjectUtils.asMap( // ObjectUtils.asMap sinh map chứa 3 thông tin bắt buộc khi gọi API.
				"cloud_name", cloudName,
				"api_key", apiKey,
				"api_secret", apiSecret
		);
		return new Cloudinary(config);
	}
}
