package javaproject.exception;


import javaproject.Laboratory;

public class NameNotFoundException extends RuntimeException {
	
	/**
	 * Throw a name not found exception if there is no item with the given name in the lab
	 * 
	 * @param	name
	 * 			The name of the ingredient we're searching
	 * @param	lab
	 * 			The lab in which we're searching for the ingredient
	 * @post	The new lab is set to the given lab
	 * 			| new.getLab() = lab
	 * @post	The new name is set to the given name
	 * 			| new.getName() = name
	 */
	public NameNotFoundException(String name, Laboratory lab) {
		this.lab = lab;
		this.name = name;
	}
	
	/**
	 * The lab in which we're searching
	 */
	private Laboratory lab = null;
	
	/**
	 * The name which we're searching for
	 */
	private String name = "";
	
	/**
	 * Return the lab in which this error was thrown
	 */
	public Laboratory getLab() {
		return lab;
	}
	
	/**
	 * Return the name on which this error was thrown
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Serial Id
	 */
	private static final long serialVersionUID = 1L;	


}
