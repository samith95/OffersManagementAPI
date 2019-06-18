package api.util;

public final class Consts {
	public static final String TIMEFORMAT = "dd/MM/yyyy";
	
	  private Consts(){
		    //this prevents even the native class from 
		    //calling this constructor as well :
		    throw new AssertionError();
	  }
}
