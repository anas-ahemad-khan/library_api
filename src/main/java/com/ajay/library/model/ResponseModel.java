package com.ajay.library.model;

public class ResponseModel {
String message;
boolean status;
Object data;
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public boolean isStatus() {
	return status;
}
public void setStatus(boolean status) {
	this.status = status;
}
public Object getData() {
	return data;
}
public boolean getStatus() {
	return status;
}
public void setData(Object data) {
	this.data = data;
}
}
