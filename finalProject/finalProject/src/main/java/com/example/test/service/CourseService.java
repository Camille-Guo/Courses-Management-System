package com.example.test.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.model.Course;
import com.example.test.repo.CourseRepo;



@Service
@Transactional
public class CourseService {
	@Autowired
	private CourseRepo courseRepo;
	
	//get all
	public List<Course> listAll(){
		return courseRepo.findAll();
	}
	
	//create
	public void save(Course course) {
		courseRepo.save(course);
	}
	
	//delete
	public void delete(String id) {
		courseRepo.deleteById(id);
	}

	//edit  / update
	public Course get(String id) {
		return courseRepo.findById(id).get();
	}

	public List<Course> listSearchCourse(String search_field) {
		return courseRepo.searchCourse(search_field);

	}
}

