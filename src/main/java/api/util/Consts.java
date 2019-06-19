package api.util;

import java.time.format.DateTimeFormatter;

public final class Consts {
	
	//constant used to format data throughout the api, done to keep consistent date format
	public static final String TIMEFORMAT = "dd/MM/yyyy";
	
    //date formatter used in package to format LocalDate variables from strings or viceversa
	public static final DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern(TIMEFORMAT);
	
	
	  private Consts(){
		    //this prevents even the native class from 
		    //calling this constructor as well :
		    throw new AssertionError();
	  }
}
