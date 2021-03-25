package mg.ituproject.stm.utils.exceptions;

public class ControlException extends Exception {
    public ControlException(String string, String fieldName){
        super(string);
        this.setFieldName(fieldName);
    }
    
    // Fields
    protected String fieldName;
    
    // Getters
    public String getFieldName() {
        return fieldName;
    }
    
    // Setters
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
}