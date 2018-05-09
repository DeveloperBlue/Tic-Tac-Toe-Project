
@SuppressWarnings("serial")
public class IllegalMoveException extends Exception {
	
	public IllegalMoveException(){
		super("An illegal move was attempted");
	}

	public IllegalMoveException(String message){
		super(message);
	}
	
}