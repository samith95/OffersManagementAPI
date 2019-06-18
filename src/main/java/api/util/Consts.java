package api.util;

public final class Consts {
	
	//constant used to format data throughout the api, done to keep consistent date format
	public static final String TIMEFORMAT = "dd/MM/yyyy";
	
	  private Consts(){
		    //this prevents even the native class from 
		    //calling this constructor as well :
		    throw new AssertionError();
	  }
}
