package com.employeemanagement.employee.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cấu hình Spring MVC để expose thư mục upload và đảm bảo thư mục tồn tại.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	@Value("${file.upload-dir}")
	private String uploadDir;

	/**
	 * Được gọi ngay sau khi bean được khởi tạo.
	 * Nếu thư mục upload chưa tồn tại thì tự động tạo để tránh lỗi khi ghi file.
	 */
	@PostConstruct
	public void creatUploadDirlNeeded(){
		try{
			Path path = Paths.get(uploadDir);
			if(!Files.exists(path)){
				Files.createDirectories(path);
			}
		}catch(Exception ex){
			log.error("Could not create upload directory at {}",uploadDir, ex);
		}
	}
	/**
	 * Map URL /images/** tới thư mục vật lý D:/uploads/images để có thể truy cập ảnh qua HTTP.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String uploadPath = Paths.get(uploadDir).toUri().toString();
		registry.addResourceHandler("/images/**")
				.addResourceLocations(uploadPath);
	}
}
/**
 *
 @Value("${file.upload-dir}") inject cấu hình từ application.properties.
 @PostConstruct createUploadDirIfNeeded():
 + Được gọi sau khi bean khởi tạo.
 + Paths.get(uploadDir) tạo Path từ chuỗi.
 + Files.notExists(path) kiểm tra tồn tại; nếu không có thì Files.createDirectories(path) tạo cả cây thư mục.
 + Bắt exception và log lỗi nếu không thể tạo (ví dụ do permissions).
 addResourceHandlers:
 + Paths.get(uploadDir).toUri().toString() chuyển đường dẫn file system thành file:/... URL.
 + registry.addResourceHandler("/images/**").addResourceLocations(uploadPath) ánh xạ tất cả request /images/** tới thư mục vật lý.
 + Cho phép truy cập file qua http://host:port/images/<file>.
 */