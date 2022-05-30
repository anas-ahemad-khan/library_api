package com.ajay.library.repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;


import com.ajay.library.model.BookModel;
import com.ajay.library.model.LendRequestModel;
import com.ajay.library.model.ResponseModel;
import com.ajay.library.model.UpdateLendModel;
import com.ajay.library.model.UserModel;
import com.ajay.library.model.LoginModel;
import com.ajay.library.model.RegisterUserModel;

@Repository
public class LibraryRepositoryImpl implements LibraryRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	public ResponseModel checkBookAvailability(String bookName) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("No books available");
		List<BookModel> booksList = new ArrayList<>();
		try {
			String bookAvailabilityQuery = "SELECT tbl_book.book_id,book_title,book_author,book_serial,book_isbn,book_edition FROM tbl_book INNER JOIN tbl_library ON  tbl_book.book_id=tbl_library.book_id JOIN tbl_book_copies ON tbl_book.book_id=tbl_book_copies.book_id WHERE tbl_library.available_copies >0 AND tbl_book_copies.availability_status=0 AND tbl_book.book_title='"
					+ bookName + "'";
			booksList = jdbcTemplate.query(bookAvailabilityQuery, BeanPropertyRowMapper.newInstance(BookModel.class));
			if (booksList.isEmpty()) {
				model.setMessage("No books found");
				return model;
			}

			model.setStatus(true);
			model.setData(booksList);
			model.setMessage("Success");
			return model;
		} catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}

	}

	@Override
	public ResponseModel checkBookAvailabilityForLend(String bookName) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("No books available");

		try {
			String bookAvailabilityQuery = "SELECT tbl_book.book_id,tbl_library.available_copies,book_edition FROM tbl_book INNER JOIN tbl_library ON  tbl_book.book_id=tbl_library.book_id WHERE  tbl_book.book_title='"
					+ bookName + "'";
			List<Map<String, Object>> result = jdbcTemplate.queryForList(bookAvailabilityQuery);
			if (result.isEmpty()) {
				model.setMessage("No books found");
				return model;
			}
			for (Map<String, Object> map : result) {
				int available_copies = (int) map.get("available_copies");
				if (available_copies == 0) {
					int book_id = (int) map.get("book_id");
					String earliestAvailabilityQuery = "SELECT MIN(return_date) AS min_date from tbl_lend WHERE book_id='"
							+ book_id + "' AND return_status=0";
					Date resultDate = jdbcTemplate.queryForObject(earliestAvailabilityQuery, Date.class);
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String minDate = dateFormat.format(resultDate);
					map.put("availableOn", minDate);
				}
			}
			model.setStatus(true);
			model.setData(result);
			model.setMessage("Success");
			return model;
		} catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}
	}

	@Override
	public ResponseModel checkStatusOfLend(String bookName, String bookSerial) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("No lend record found");

		try {
			String lendStatusQuery = "SELECT tbl_lend.date_of_issue,tbl_lend.return_date,tbl_lend.return_status FROM tbl_lend INNER JOIN tbl_book ON tbl_book.book_id=tbl_lend.book_id WHERE tbl_lend.book_serial='"
					+ bookSerial + "' AND tbl_book.book_title='" + bookName + "'";
			Map<String, Object> result = jdbcTemplate.queryForMap(lendStatusQuery);
			if (result.isEmpty()) {
				model.setMessage("No books found");
				return model;
			}

			int returnStatus = (int) result.get("return_status");
			if (returnStatus == 0) {
				result.replace("return_status", "Book not returned");
			} else {
				result.replace("return_status", "Book returned");
			}

			LocalDateTime returnDate = (LocalDateTime) result.get("return_date");
			LocalDateTime today = LocalDateTime.now();
			System.out.println(result.get("date_of_issue"));
			System.out.println(result.get("return_date"));
			long dateDifference = ChronoUnit.DAYS.between(returnDate, today);
			System.out.println("Difference" + dateDifference);

			if (dateDifference < 0) {
				result.put("remainingDays", Math.abs(dateDifference));
			} else {
				result.put("remainingDays", 0);
			}

			if (dateDifference >= 30) {
				long fine = dateDifference * 10;
				result.put("fine", fine);
			} else {
				result.put("fine", 0);
			}

			model.setStatus(true);
			model.setData(result);
			model.setMessage("Success");
			return model;
		} catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}
	}

	@Override
	public ResponseModel lendRequest(LendRequestModel requestModel) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("Failed");

		try {

			String bookDetailsQuery = "SELECT book_id FROM tbl_book WHERE book_title='" + requestModel.getBookName()
					+ "' AND book_edition='" + requestModel.getBookEdition() + "';";
			int bookID = jdbcTemplate.queryForObject(bookDetailsQuery, Integer.class);
			System.out.println("BookID:" + bookID);

			if (bookID != 0) {
				String checkBookAvailability = "SELECT available_copies FROM tbl_library WHERE book_id='" + bookID
						+ "'";
				int availableCount = jdbcTemplate.queryForObject(checkBookAvailability, Integer.class);
				if (availableCount == 0) {
					model.setMessage("No copy of book is available to lend");
					return model;
				}
				String getAvailableBooksQuery = "SELECT book_serial FROM tbl_book_copies WHERE book_id = '" + bookID
						+ "' AND availability_status=0;";
				List<Integer> serialNumberList = jdbcTemplate.queryForList(getAvailableBooksQuery, Integer.class);
				if (serialNumberList.isEmpty()) {
					model.setMessage("No copy of book is available to lend");
					return model;
				}
				int bookSerial = serialNumberList.get(0);
				LocalDate issueDate = LocalDate.now();
				String processLendRequestQuery = "INSERT INTO tbl_lend (book_id,book_serial,borrower_name,borrower_id,borrower_contact,borrower_dob,date_of_issue,return_date,return_status)  VALUES("
						+ bookID + ",'" + bookSerial + "','" + requestModel.getBookName() + "','"
						+ requestModel.getBorrowerId() + "','" + requestModel.getBorrowerContact() + "','"
						+ requestModel.getBorrowerDob() + "',str_to_date('" + issueDate.toString()
						+ "','%Y-%m-%d'),str_to_date('" + requestModel.getReturnDate() + "','%Y-%m-%d'),0);";
				int response = jdbcTemplate.update(processLendRequestQuery);
				System.out.println(response);
				if (response == 0) {
					return model;
				}
				// Update tbl_book_copies
				String updateBookStatus = "UPDATE tbl_book_copies SET availability_status=1 WHERE book_serial='"
						+ bookSerial + "' AND book_id=" + bookID;
				response = jdbcTemplate.update(updateBookStatus);

				String updateLibraryQuery = "UPDATE tbl_library SET available_copies=available_copies-1 WHERE book_id="+bookID;
				response = jdbcTemplate.update(updateLibraryQuery);
				
				model.setStatus(true);
				model.setData(response);
				model.setMessage("Book issued successfully");
				return model;
			}

			return model;
		} catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}

	}

	@Override
	public ResponseModel returnBook(UpdateLendModel updateModel) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("Failed");
		try {
			String bookDetailsQuery = "SELECT book_id FROM tbl_book WHERE book_title='" + updateModel.getBookName()
			+ "' AND book_edition='" + updateModel.getBookEdition() + "';";
			int bookID = jdbcTemplate.queryForObject(bookDetailsQuery, Integer.class);
			String updateLendQuery = "UPDATE tbl_lend SET return_status=1 WHERE book_id="+bookID+" AND book_serial='"+updateModel.getBookSerial()+"' AND borrower_name='"+updateModel.getBorrowerName()+"' AND borrower_id='"+updateModel.getBorrowerId()+"'";
			int response = jdbcTemplate.update(updateLendQuery);
			String updateBookQuery = "UPDATE tbl_book_copies SET availability_status=0 WHERE book_id="+bookID+" AND book_serial='"+updateModel.getBookSerial()+"'";
			response= jdbcTemplate.update(updateBookQuery);
			String updateLibraryQuery = "UPDATE tbl_library SET available_copies=available_copies+1 WHERE book_id="+bookID;
			response = jdbcTemplate.update(updateLibraryQuery);
			model.setStatus(true);
			model.setMessage("Book returned");
			model.setData(response);
			return model;
		}catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}

	}

	@Override
	public ResponseModel deleteRecord(UpdateLendModel updateModel) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("Failed");
		try {
			String bookDetailsQuery = "SELECT book_id FROM tbl_book WHERE book_title='" + updateModel.getBookName()
			+ "' AND book_edition='" + updateModel.getBookEdition() + "';";
			int bookID = jdbcTemplate.queryForObject(bookDetailsQuery, Integer.class);
			String updateLendQuery = "DELETE from tbl_lend  WHERE book_id="+bookID+" AND book_serial='"+updateModel.getBookSerial()+"' AND borrower_name='"+updateModel.getBorrowerName()+"' AND borrower_id='"+updateModel.getBorrowerId()+"'";
			int response = jdbcTemplate.update(updateLendQuery);
			model.setStatus(true);
			model.setMessage("Record deleted");
			model.setData(response);
			return model;
		}catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}
	}

	@Override
	public ResponseModel authorizeUser(LoginModel loginModel) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("Failed");
		try {
		
			String loginQuery = "SELECT username,user_type FROM tbl_login WHERE username='ajay' AND PASSWORD='password';";
			UserModel userModel= jdbcTemplate.queryForObject(loginQuery, BeanPropertyRowMapper.newInstance(UserModel.class));
			if(userModel!=null) {
//				String token = new JwtTokenUtil().generateToken(loginModel);
//				System.out.println(token);
//				userModel.setToken(token);
				model.setStatus(true);
				model.setMessage("Success");
				model.setData(userModel);
				return model;
			}
			else {
				model.setMessage("No user found");
				return model;	
			}
			
		}catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}
	}

	@Override
	public UserModel loadUserByUsername(String username) {
		try {
		
			String loginQuery = "SELECT username,user_type,password FROM tbl_login WHERE username='"+username+"'";
			UserModel userModel= jdbcTemplate.queryForObject(loginQuery, BeanPropertyRowMapper.newInstance(UserModel.class));
			if(userModel!=null) {

				return userModel;
			}
			else {
				
				return null;	
			}
			
		}catch (Exception e) {
			return null;

		}
	}

	@Override
	public String getUserTypeByUsername(String username) {
		try {
			
			String loginQuery = "SELECT user_type FROM tbl_login WHERE username='"+username+"'";
			String userType= jdbcTemplate.queryForObject(loginQuery, String.class);
			if(userType!=null) {

				return userType;
			}
			else {
				
				return null;	
			}
			
		}catch (Exception e) {
			return null;

		}
	}

	@Override
	public ResponseModel registerUser(RegisterUserModel userModel) {
		ResponseModel model = new ResponseModel();
		model.setStatus(false);
		model.setMessage("Failed");
		try {
			
			String registerUserQuery = "Insert into tbl_login ('"+userModel.getUsername()+"','"+userModel.getPassword()+"','"+userModel.getUserType()+"')";
			int result = jdbcTemplate.update(registerUserQuery);
			if(result != 0) {
				model.setStatus(true);
				model.setMessage("Success");
				model.setData(result);
				return model;
			}
			
		}catch (Exception e) {
			model.setMessage(e.toString());
			return model;

		}
		return null;
	}



}
