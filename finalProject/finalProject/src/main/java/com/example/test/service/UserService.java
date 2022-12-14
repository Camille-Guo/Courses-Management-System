package com.example.test.service;

import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.test.model.Course;

import com.example.test.model.User;
import com.example.test.repo.CourseRepo;

import com.example.test.repo.UserRepo;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public void saveUser(User user) {
		userRepo.save(user);
	}

	public User getUser(Long id) {
		return userRepo.findById(id).get();
	}

	public void deleteUser(Long id) {
		userRepo.deleteById(id);
	}

	public User getUserByEmail() {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		return userRepo.findByEmail(userDetails.getUsername());
	}

	public List<User> listAllStudents() {

		return userRepo.findAllStudents();
	}

	public List<User> listAllTeachers() {
		// TODO Auto-generated method stub
		return userRepo.findAllTeachers();
	}
	
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepo.findAll();
	}

	@Autowired
	private CourseRepo courseRepo;

	public List<Course> listAllCourses() {
		return courseRepo.findAll();
	}

	public void saveCourse(Course course) {
		courseRepo.save(course);
	}

	public Course getCourse(String id) {
		return courseRepo.findById(id).get();
	}

	public void deleteCourse(String id) {
		courseRepo.deleteById(id);
	}

	public List<User> listRecords() {
		List<User> users = userRepo.findAll();
		
		List<User> user = users.stream().filter(c -> c.getCourse().size() > 0).collect(Collectors.toList());


		return user;

	}

	public Course getOneCourse(String paraValue) {

		Course course = courseRepo.findOneCourse(paraValue);

		return course;
	}

	public User getOneUser(String paraValue) {
		 try {
			  Long.valueOf(paraValue);
		 } 
		 catch(NumberFormatException e){
			  
			 User user = userRepo.findOneUserByName(paraValue); 
			 return user;
		 }
		 Long id = Long.valueOf(paraValue);
		 User user= getUser(id);
		 return user;
	}

	

	public List<User> getByStudent(String paraValue) {
		List<User> listUsers = new ArrayList<>();
		User user = getOneUser(paraValue);
		listUsers.add(user);
		return listUsers;
	}

	public List<Course> getByCourse(String paraValue) {
		List<Course> listCourses = new ArrayList<>();
		User user = getOneUser(paraValue);
		if (user == null) {
			Course course = getOneCourse(paraValue);
			listCourses.add(course);
		} else {
			List<Course> listCourse = courseRepo.findListCourseByTeacher(paraValue);
			listCourses.addAll(listCourse);
		}

		return listCourses;
	}


	public List<Course> getCourseInCharge(String paraValue) {
		List<Course> listCourses = courseRepo.findListCourseByTeacher(paraValue);
		return listCourses;
	}
}
