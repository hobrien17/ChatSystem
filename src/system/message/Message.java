package system.message;

import java.io.*;

public class Message implements Serializable {
	
	private static final long serialVersionUID = -6273916090125213780L;
	
	public static enum Type {
		MESSAGE, USERNAME, LOGOUT, END
		//more types can be added should they be needed
	}

	String message;
	Type type;
	
	public Message(String msg, Type type) {
		message = msg;
		this.type = type;
	}
	
	@Override
	public String toString() {
		return message;
	}
	
	public Type getType() {
		return type;
	}
}
