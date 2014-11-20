package com.psychocoder.applet;

public final class IllegalCharacterException extends Exception {

	private static final long serialVersionUID = 1L;
	private String message = "Illegal instruction or character found";
	public IllegalCharacterException(){
		super();
	}
	
	public IllegalCharacterException(String message){
		super(message);
	}
	
	public IllegalCharacterException(Throwable cause){
		super(cause);
	}
	
	/* 
	 * @see java.lang.Throwable#toString()
	 */
	
	@Override
	public String toString(){
		return message;
	}
	
	/* 
	 * @see java.lang.Throwable#getMessage()
	 */
	
	@Override
	public String getMessage(){
		return message;
	}

}
