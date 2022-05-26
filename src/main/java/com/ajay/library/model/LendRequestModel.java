package com.ajay.library.model;

public class LendRequestModel {

	String borrowerName;
	String borrowerId;
	String borrowerContact;
	String borrowerDob;
	String bookName;
	String bookEdition;
	String returnDate;
	
	
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getBorrowerName() {
		return borrowerName;
	}
	public void setBorrowerName(String borrowerName) {
		this.borrowerName = borrowerName;
	}
	public String getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(String borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getBorrowerContact() {
		return borrowerContact;
	}
	public void setBorrowerContact(String borrowerContact) {
		this.borrowerContact = borrowerContact;
	}
	public String getBorrowerDob() {
		return borrowerDob;
	}
	public void setBorrowerDob(String borrowerDob) {
		this.borrowerDob = borrowerDob;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookEdition() {
		return bookEdition;
	}
	public void setBookEdition(String bookEdition) {
		this.bookEdition = bookEdition;
	}
	
}
