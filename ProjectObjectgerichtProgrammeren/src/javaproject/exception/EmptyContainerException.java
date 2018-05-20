package javaproject.exception;

import be.kuleuven.cs.som.annotate.Raw;

public class EmptyContainerException extends RuntimeException {
	
	/**
	 * Throw an empty container exception if the container which is added is empty
	 */
	@Raw
	public EmptyContainerException() {
		
	}

	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 1L;

}
