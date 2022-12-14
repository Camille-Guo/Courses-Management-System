package com.example.test.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.test.model.Course;

public interface CourseRepo extends JpaRepository<Course,String>{
	
	@Query("SELECT c FROM Course c WHERE c.courseName = ?1 or c.courseId = ?1")
	Course findOneCourse(String paraValue);
	
	
	@Query("SELECT c FROM Course c WHERE c.teacher= ?1")
	List<Course> findListCourseByTeacher(String paraValue);

	@Query("SELECT c FROM Course c WHERE ( c.courseName like concat('%', ?1, '%') or c.courseId like concat('%', ?1, '%'))")
	public List<Course> searchCourse(String search_field);

}


