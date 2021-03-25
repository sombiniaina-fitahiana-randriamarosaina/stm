package mg.ituproject.stm.utils.webServices;

public class WebServiceObject{
	// Construstor
	public WebServiceObject(int status, String message, Object data) {
		this.setMeta(new Meta(status, message));
		this.data = data;
	}
	
	// Field
	private Meta meta;
	private Object data;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
	
	
}