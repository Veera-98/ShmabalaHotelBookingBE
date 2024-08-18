package com.example.daang.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.daang.Model.User;
import com.example.daang.Repository.UserRepository;
import com.example.daang.Service.LoginService;

@RestController
@RequestMapping("/api/hotel-staff")
@CrossOrigin
public class LoginController {
	
	
	
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private LoginService loginService;
	@PostMapping("/user")
    public ResponseEntity<String> addlogin(@RequestBody User user) {

        

     
		userRepository.save(user);

        return ResponseEntity.ok("login credentails added succcesufully");
    }

	
	@PostMapping("/validate/login")
	public ResponseEntity<Integer> validateLogin(@RequestBody User user) {
	    List<User> loginList = loginService.getAllLogins();
	    
	    
	    Optional<User> matchedLogin = loginList.stream()
	            .filter(l -> null!=l.getUserid()&&l.getUserid().equals(user.getUserid()) && l.getPassword().equals(user.getPassword()))
	            .findFirst();

	    if (matchedLogin.isPresent()) {
	        // If a match is found, return the roleId
	        return ResponseEntity.ok(matchedLogin.get().getRole_id());
	    } else {
	        // If no match is found, return an error status
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	}


}
