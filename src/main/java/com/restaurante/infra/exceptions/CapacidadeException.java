package com.restaurante.infra.exceptions;

public class CapacidadeException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public CapacidadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CapacidadeException(String message) {
		super(message);
	}

}
