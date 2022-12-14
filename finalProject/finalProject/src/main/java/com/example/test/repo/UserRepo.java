package com.example.test.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.test.model.User;



public interface UserRepo extends JpaRepository<User,Long>{

	
	@Query("SELECT u FROM User u WHERE u.email= ?1")
	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.type ='student'")
	public List<User> findAllStudents();

	@Query("SELECT u FROM User u WHERE u.type ='teacher'")
	public List<User> findAllTeachers();
	
	
	@Query("SELECT u FROM User u WHERE u.name= ?1")
	public User findOneUserByName(String paraValue);
	
	@Query("SELECT u FROM User u WHERE u.type='student' and( u.name like concat('%', ?1, '%') or u.userId like concat('%', ?1, '%'))")
	public List<User> searchStudent(String search_field);

}

