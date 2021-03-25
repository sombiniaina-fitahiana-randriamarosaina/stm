package mg.ituproject.stm.utils.exceptions;

public class ValidateException extends Exception {
    public ValidateException(String string, Object data){
        super(string);
        this.data = data;
    }
    
    // Fields
    private Object data;
    
    public Object getData() {
    	return this.data;
    }
}
