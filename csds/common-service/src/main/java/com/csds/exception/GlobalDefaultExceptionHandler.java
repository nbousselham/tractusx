package com.csds.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.csds.constant.ApplicationMessageConstant;
import com.csds.response.ResponseObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseObject> handleAllException(Exception ex, WebRequest request) {
		ResponseObject response = new ResponseObject();
		response.setStatus(ApplicationMessageConstant.FAILED);
		response.setMessage("Internal server error while processing request");
		log.error(ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<ResponseObject> handleNodataFoundException(NoDataFoundException ex, WebRequest request) {
		ResponseObject body = new ResponseObject();
		log.error(ex.getMessage());
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ResponseObject> handleServiceException(ServiceException ex, WebRequest request) {
		ResponseObject response = new ResponseObject();
		response.setStatus(ApplicationMessageConstant.FAILED);
		response.setMessage("Internal server error while processing request");
		log.error(ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ResponseObject> handleIOException(ValidationException ex, WebRequest request) {

		ResponseObject response = new ResponseObject();
		response.setStatus(ApplicationMessageConstant.FAILED);
		response.setMessage(ex.getMessage());
		log.error(ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

	}

}