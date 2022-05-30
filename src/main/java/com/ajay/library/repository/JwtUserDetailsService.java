package com.ajay.library.repository;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ajay.library.model.UserModel;



@Service
public class JwtUserDetailsService implements UserDetailsService {

	
	@Autowired
	private LibraryRepository libraryRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserModel user=libraryRepository.loadUserByUsername(username);
		
		if (user!=null) {
			UserDetails userDetails= new User(user.getUsername(), user.getPassword(),
					new ArrayList<>());
			return userDetails;
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}