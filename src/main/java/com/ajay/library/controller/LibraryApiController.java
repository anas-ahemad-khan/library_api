package com.ajay.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajay.library.model.LendRequestModel;
import com.ajay.library.model.ResponseModel;
import com.ajay.library.model.UpdateLendModel;
import com.ajay.library.repository.LibraryRepository;



@RestController
@RequestMapping("/api")
public class LibraryApiController {
	@Autowired
	LibraryRepository repository;
	
	
	//Case 1:
	@GetMapping("/checkBookAvailability")
	public ResponseEntity<Object> checkBookAvailability(@RequestParam("bookName") String bookName) {
		try {
			ResponseModel responseModel = repository.checkBookAvailability(bookName);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
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
	public ResponseEntity<Object> lendRequest(@RequestBody LendRequestModel model) {
		try {
			ResponseModel responseModel = repository.lendRequest(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 5:
	@PutMapping("/returnBook")
	public ResponseEntity<Object> returnBook(@RequestBody UpdateLendModel model) {
		try {
			ResponseModel responseModel = repository.returnBook(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//Case 6:
	@DeleteMapping("/deleteRecord")
	public ResponseEntity<Object> deleteRecord(@RequestBody UpdateLendModel model) {
		try {
			ResponseModel responseModel = repository.deleteRecord(model);
			return new ResponseEntity<>(responseModel, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
