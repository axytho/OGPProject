package javaproject.exception;

public class ExceedsStorageException extends RuntimeException {

	/**
	 * Serial id
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize a storage exception which is triggered by asking more than is available in storage
	 * 
	 * @param	amountAsked
	 * 			The amount asked
	 * @param	amountAvailable
	 * 			The amount available
	 * @post	The amount asked is set to the given amount asked
	 * 			| new.getAmountAsked() == amountAsked
	 * @post	The amount available is set to the given amount available
	 * 			| new.getAmountAvailable() == amountAvailable
	 */
	public ExceedsStorageException(int amountAsked, int amountAvailable) {
		this.amountAsked = amountAsked;
		this.amountAvailable = amountAvailable;
	}

	/**
	 * The amount asked
	 */
	private int amountAsked = 0;
	
	/**
	 * The amount that is available
	 */
	private int amountAvailable = 0;
	
	/**
	 * Return the amount asked
	 */
	public int getAmountAsked() {
		return amountAsked;
	}
	
	/**
	 * Return the amount available
	 */
	public int getAmountAvailable() {
		return amountAvailable;
	}
}
