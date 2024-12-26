package com.saransh.ecom_proj.exceptions;

public class ProductNotFoundException extends RuntimeException{
	public ProductNotFoundException(String message) {
		super(message);
	}
}
