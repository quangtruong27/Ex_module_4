create database employee_management;

create table department(
	id int primary key auto_increment,
    name varchar(50) not null unique
);

create table employee(
	id int primary key auto_increment,
    name varchar(100) not null,
    dob date,
    gender enum('MALE', 'FEMALE','OTHER'),
    salary double,
    phone varchar(15) unique,
    department_id int,

    CONSTRAINT fk_employee_department
    FOREIGN KEY (department_id)
    REFERENCES department(id)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

INSERT INTO department (name)
VALUES
('IT'),
('HR'),
('Marketing'),
('Finance');


INSERT INTO employee (name, dob, gender, salary, phone, department_id)
VALUES
('Do Mixi', '2003-09-10', 'MALE', 1000, '0123456789', 1),
('Donald Trump', '2002-05-01', 'FEMALE', 1200, '0987654321', 2),
('Putin', '2001-12-20', 'MALE', 900, '0911222333', 1),
('Ronaldo', '2003-03-15', 'FEMALE', 1100, '0933444555', 3);
