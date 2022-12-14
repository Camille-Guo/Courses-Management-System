package com.example.test.security;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.test.model.User;
import com.example.test.repo.UserRepo;





public class CustomerUserDetailsService implements UserDetailsService {
@Autowired
private UserRepo userRepo;
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
	// get this object from database
	User user = userRepo.findByEmail(username);
if (user == null) {
throw new UsernameNotFoundException("user not found"); 
}



return new CustomerUserDetails(user); 
}
}