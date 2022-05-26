package com.ajay.library.model;

public class UpdateLendModel {
String borrowerName;
String borrowerId;
String bookName;
String bookSerial;
String bookEdition;

public String getBookEdition() {
	return bookEdition;
}
public void setBookEdition(String bookEdition) {
	this.bookEdition = bookEdition;
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
public String getBookName() {
	return bookName;
}
public void setBookName(String bookName) {
	this.bookName = bookName;
}
public String getBookSerial() {
	return bookSerial;
}
public void setBookSerial(String bookSerial) {
	this.bookSerial = bookSerial;
}

}
