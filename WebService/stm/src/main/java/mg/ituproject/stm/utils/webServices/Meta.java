package mg.ituproject.stm.utils.webServices;

public class Meta {
	// Constructor
	public Meta(int status, String message) {
		this.setStatus(status);
		this.setMessage(message);
	}
	// Fields
	protected int status;
	protected String message;
	
	// Getters & Setters
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		return String.format("{status : %s, message : %s}", getStatus(), getMessage());
	}
}
