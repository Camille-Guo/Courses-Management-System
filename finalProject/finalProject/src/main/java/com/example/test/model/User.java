package com.example.test.model;


import java.time.LocalDate;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;




@Entity
@GenericGenerator(name="user-uuid", strategy = "uuid")
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue
	private Long userId;
	
	@NotBlank
	@Column(nullable=false)
	private String name;
	
	
	@NotBlank
    @Email(message = "Please enter a valid email address")
	@Column(nullable=false, unique=true)
	private String email;
	
	
    @Pattern(regexp="^[0-9]{10}$",message="must be exactly 10 digits!")  
    @Column(nullable=false)
	private String phone;
    
	@NotBlank
	@Column(nullable=false)
	private String address;
	
	@Size(min=8,max=64,message="must be between 8 and 20 characters!")
	@Column(nullable=false)
	private String password;
	
	@Size(min=8,max=64,message="must be between 8 and 20 characters!")
	@Column(nullable=false)
	transient private String retypePassword;
	
	public String getRetypePassword() {
		return retypePassword;
	}

	public void setRetypePassword(String retypePassword) {
		this.retypePassword = retypePassword;
	}

	@Column(nullable=false)
	private String type;
	
	
	@Column(nullable=true)
	private LocalDate expireDate = LocalDate.now().plusYears(1);
	
	@ManyToMany(targetEntity = Course.class,cascade=CascadeType.ALL)
	@JoinTable(name="records",
	joinColumns= {@JoinColumn(name="userId",referencedColumnName = "userId")},
	inverseJoinColumns={@JoinColumn(name="courseId",referencedColumnName = "courseId")})
	private List<Course> course;

	public List<Course> getCourse() {
		return course;
	}

	public void setCourse(List<Course> course) {
		this.course = course;
	}
	
	public Long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getPassword() {
		return password;
	}
    
	public String getType() {
		return type;
	}

	public LocalDate getExpireDate() {
		return expireDate;
	}

//	public List<Courses> getCourses() {
//		return courses;
//	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

//	public void setCourses(List<Courses> courses) {
//		this.courses = courses;
//	}

	
}