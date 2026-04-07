package com.employeemanagement.employee.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.persistence.*;
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
	@ManyToOne
	Department department;
}
