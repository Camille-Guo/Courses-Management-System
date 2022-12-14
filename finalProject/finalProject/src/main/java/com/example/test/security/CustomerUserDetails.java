package com.example.test.security;

import java.util.ArrayList;
import java.util.Collection;

import java.util.List;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.test.model.User;


@SuppressWarnings("serial")
public class CustomerUserDetails implements UserDetails {

	private User user;
	Collection<? extends GrantedAuthority> authorities; 

	public CustomerUserDetails( User user) {
		this.user = user;
		}
    
	 public Collection<? extends GrantedAuthority> getAuthorities() {
		 
	        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	         
	        authorities.add(new SimpleGrantedAuthority(user.getType()));
	   
	        return authorities;
	    }
	
	

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getName() {
		return user.getName() ;
	}

}

