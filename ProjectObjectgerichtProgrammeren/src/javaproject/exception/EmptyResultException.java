package javaproject.exception;

import be.kuleuven.cs.som.annotate.Raw;

public class EmptyResultException extends RuntimeException {
	
	/**
	 * Throw an empty result exception if the result you're trying to get is empty
	 */
	@Raw
	public EmptyResultException() {
		
	}

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 1L;	

}
