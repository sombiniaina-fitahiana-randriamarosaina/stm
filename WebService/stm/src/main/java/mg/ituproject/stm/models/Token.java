package mg.ituproject.stm.models;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.bson.Document;

import mg.ituproject.stm.utils.databases.MongoDocument;
import mg.ituproject.stm.utils.databases.MongoHelper;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Token implements MongoDocument{
	protected String idPersonne;
	protected String token;
	
	public Token() {}
	
	public Token(String idPersonne,String token) 
	{
		setIdPersonne(idPersonne);
		setToken(token);
	}
	public void setIdPersonne(String id) 
	{
		this.idPersonne=id;
	}
	public void setToken(String token) 
	{
		this.token=token;
	}
	public String getIdPersonne() 
	{
		return this.idPersonne;
	}
	public String getToken() 
	{
		return this.token;
	}
	
	public static Token generateToken(Client client) 
	{
		String token = DigestUtils.sha1Hex(client.getNomClient() + new Date().toString()); 
		return new Token(client.getIdClient(), token);
	}
	
	public static Token generateToken(Admin admin) 
	{
		String token = DigestUtils.sha1Hex(admin.getPseudo() + new Date().toString()); 
		return new Token(admin.getIdAdmin(), token);
	}

	public void insert(MongoHelper mongoHelper){
		mongoHelper.insert("Token", this.toDocument());
	}
	
	@Override
	public Document toDocument() {
		Document doc = new Document("idPersone", getIdPersonne()).append("token", getToken());
		return doc;
	}
	
}
