package com.grupo4.projetointegrador.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;

	private List<FieldMessenger> errors = new ArrayList<>();
	
	public ValidationError() {}
	
	public ValidationError(Long timestamp, Integer status, String error) {
		super(timestamp, status, error);
	}
	
	public List<FieldMessenger> getErrors() {
		return errors;
	}
	
	public void addError(String fieldName, String menssage) {
		this.errors.add(new FieldMessenger(fieldName, menssage));
	}
	
}
