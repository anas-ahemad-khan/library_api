package com.ajay.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajay.library.config.JwtTokenUtil;
import com.ajay.library.model.LendRequestModel;
import com.ajay.library.model.ResponseModel;
import com.ajay.library.model.UpdateLendModel;
import com.ajay.library.model.UserModel;
import com.ajay.library.model.LoginModel;
import com.ajay.library.model.RegisterUserModel;
import com.ajay.library.repository.LibraryRepository;



@RestController
@CrossOrigin

@RequestMapping("/api")
public class LibraryApiController {
	@Autowired
	LibraryRepository repository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	
	//Case 1:
	@GetMapping("/checkBookAvailability")
	public ResponseEntity<Object> checkBookAvailability(@RequestParam("bookName") String bookName) {
		try {
			ResponseModel responseModel = repository.checkBookAvailability(bookName);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 2:
	@GetMapping("/checkBookAvailabilityForLend")
	public ResponseEntity<Object> checkBookAvailabilityForLend(@RequestParam("bookName") String bookName) {
		try {
			ResponseModel responseModel = repository.checkBookAvailabilityForLend(bookName);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 3:
	@GetMapping("/checkStatusOfLend")
	public ResponseEntity<Object> checkStatusOfLend(@RequestParam("bookName") String bookName,@RequestParam("bookSerial") String bookSerial) {
		try {
			ResponseModel responseModel = repository.checkStatusOfLend(bookName,bookSerial);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 4:
	@PostMapping("/lendRequest")
	public ResponseEntity<Object> lendRequest(@RequestHeader("Authorization")String token,@RequestBody LendRequestModel model) {
		try {
			token= token.replace("Bearer", "").trim();
			String user =jwtTokenUtil.getUsernameFromToken(token);
			String userType= getUserTypeByUsername(user);
			if(userType.equals("librarian")) {
			ResponseModel responseModel = repository.lendRequest(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<>("You are not authorized to do this operation", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 5:
	@PutMapping("/returnBook")
	public ResponseEntity<Object> returnBook(@RequestHeader("Authorization")String token,@RequestBody UpdateLendModel model) {
		try {
			token= token.replace("Bearer", "").trim();
			String user =jwtTokenUtil.getUsernameFromToken(token);
			String userType= getUserTypeByUsername(user);
			if(userType.equals("librarian")) {
			ResponseModel responseModel = repository.returnBook(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
			}else {
				return new ResponseEntity<>("You are not authorized to do this operation", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 6:
	@DeleteMapping("/deleteRecord")
	public ResponseEntity<Object> deleteRecord(@RequestHeader("Authorization")String token,@RequestBody UpdateLendModel model) {
		try {
			token= token.replace("Bearer", "").trim();
			String user =jwtTokenUtil.getUsernameFromToken(token);
			String userType= getUserTypeByUsername(user);
			if(userType.equals("librarian")) {
			ResponseModel responseModel = repository.deleteRecord(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
			}else {
				return new ResponseEntity<>("You are not authorized to do this operation", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Authorization
	@PostMapping("/authorizeUser")
	public ResponseEntity<Object> authorizeUser(@RequestBody LoginModel model) {
		try {
			
			ResponseModel result= repository.authorizeUser(model);
			if(result.getStatus()) {
			authenticate(model.getUsername(), model.getPassword());
			final UserDetails userDetails = jwtInMemoryUserDetailsService
					.loadUserByUsername(model.getUsername());

			final String token = jwtTokenUtil.generateToken(userDetails);
			UserModel responseModel = (UserModel) result.getData();
			responseModel.setToken(token);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);

			}else {
				return new ResponseEntity<>(result.getMessage(), HttpStatus.OK);
			}

			
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/registerUser")
	public ResponseEntity<Object> registerUser(@RequestHeader("Authorization")String token,@RequestBody RegisterUserModel model) {
		try {
			token= token.replace("Bearer", "").trim();
			
			String user =jwtTokenUtil.getUsernameFromToken(token);
			String userType= getUserTypeByUsername(user);
			if(userType.equals("admin")) {
			ResponseModel responseModel = repository.registerUser(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
			}else {
				return new ResponseEntity<>("You are not authorized to do this operation", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	
	private String getUserTypeByUsername(String username) {
		try {
			String userType = repository.getUserTypeByUsername(username);
			return userType;
		}catch(Exception e) {
			return null;
		}
	}
	
	
}
