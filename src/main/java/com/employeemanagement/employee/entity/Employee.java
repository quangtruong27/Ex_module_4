package com.employeemanagement.employee.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	LocalDate dob;
	@Enumerated(EnumType.STRING)
	Gender gender;
	Double salary;
	String phone;

	String avatarUrl; //  Đường dẫn ảnh trên Cloudinary để FE hiển thị.
	String avatarPublicId; // public_id do Cloudinary cấp, dùng để xoá/cập nhật file.

	@ManyToOne
	Department department;

	/**
	 * Avatar được lưu trực tiếp trong DB dưới dạng BLOB.
	 * Đánh dấu LAZY để tránh load byte[] khi chỉ cần thông tin employee cơ bản.
	 */
	@Lob // giúp Hibernate hiểu đây là dữ liệu nhị phân lớn.
	@Basic(fetch = FetchType.LAZY) // FetchType.LAZY cực kỳ quan trọng: tránh việc load byte[] không cần thiết (giúp query nhanh hơn).
	@Column(name = "avatar_blob", columnDefinition = "LONGBLOB")
	byte[] avatarBlob; // Dùng byte[] để Spring Boot map trực tiếp vào BLOB.

	// Local server
	String avatar;
}
