package com.ajay.library.repository;

import com.ajay.library.model.LendRequestModel;
import com.ajay.library.model.ResponseModel;
import com.ajay.library.model.UpdateLendModel;

public interface LibraryRepository {

	public ResponseModel checkBookAvailability(String bookName);
	public ResponseModel checkBookAvailabilityForLend(String bookName);
	public ResponseModel checkStatusOfLend(String bookName,String bookSerial);
	public ResponseModel lendRequest(LendRequestModel requestModel);
	public ResponseModel returnBook(UpdateLendModel updateModel);
	public ResponseModel deleteRecord(UpdateLendModel updateModel);

	
}