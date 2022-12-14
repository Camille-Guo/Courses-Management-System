package com.example.test.model;



import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name="course")
public class Course {
	
	@Id 
	private String courseId ;
	
	@NotBlank
	@Column(nullable=false)
	private String courseName;
	
	@NotBlank
	@Column(nullable=false)
	private String teacher;
	
    @Min(value=0)
	@Column(nullable=false)
	private Integer spotsLeft;
	

	@Column(nullable=false)
	private Date startDate;
	

	@Column(nullable=false)
	private Date endDate;
	
	
	@Column(nullable=false)
	private Date deadline;
	
	 
	@Column(nullable=true)
	private String description;
	
	@ManyToMany(mappedBy="course",cascade= {CascadeType.ALL})
    private List<User> users;

	public String getCourseId() {
		return courseId;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getCourseName() {
		return courseName;
	}

	public String getTeacher() {
		return teacher;
	}

	public Integer getSpotsLeft() {
		return spotsLeft;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public Date getDeadline() {
		return deadline;
	}

	public String getDescription() {
		return description;
	}

//	public List<User> getUsers() {
//		return users;
//	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	public void setSpotsLeft(Integer spotsLeft) {
		this.spotsLeft = spotsLeft;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public void setDescription(String description) {
		this.description = description;
	}

//	public void setUsers(List<User> users) {
//		this.users = users;
//	}
	
	
}