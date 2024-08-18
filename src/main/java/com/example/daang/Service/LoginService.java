package com.example.daang.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daang.Model.User;
import com.example.daang.Repository.UserRepository;

@Service
public class LoginService {
	
	
	 @Autowired
	    private UserRepository userRepository;

	    public List<User> getAllLogins() {
	        return userRepository.findAll();
	    }

}
