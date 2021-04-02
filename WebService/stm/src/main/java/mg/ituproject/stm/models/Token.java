package mg.ituproject.stm.models;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import mg.ituproject.stm.utils.exceptions.ControlException;

public class Token{
	@Id
	protected String id;
	protected String idPersonne;
	protected String token;
	
	public Token() {}
	
	public Token(String idPersonne,String token) 
	{
		setIdPersonne(idPersonne);
		setToken(token);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return new Token(admin.getId(), token);
	}

	public void insertAdmin(MongoTemplate mongoTemplate){
		mongoTemplate.insert(this, "TokenAdmin");
	}
	public void insertClient(MongoTemplate mongoTemplate){
		mongoTemplate.insert(this, "TokenClient");
	}
	
	public static String extract (HttpServletRequest request) throws ControlException {
		String header = request.getHeader("Authorization");
		if(header == null)
			throw new ControlException("Token absent", "token");
		String[] authHeaders = header.split(" ");
		for (int i = 0; i < authHeaders.length; i++)
			if(authHeaders[i].compareToIgnoreCase("bearer") == 0)
				return authHeaders[i+1];
		throw new ControlException("Token absent", "token"); 
	}
	
	public static boolean estConnecteAdmin(MongoTemplate mongoTemplate, String stringToken) {
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(stringToken));
		Token token = mongoTemplate.findOne(query, Token.class, "TokenAdmin");
		return (token != null);
	}
	
	public static boolean estConnecteCompteClient(MongoTemplate mongoTemplate, String idClient, String stringToken) {
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(stringToken)
				.andOperator(Criteria.where("idPersonne").is(idClient))
		);
		Token token = mongoTemplate.findOne(query, Token.class, "TokenClient");
		return (token != null);
	}
	
	public static boolean estConnecteClient(MongoTemplate mongoTemplate, String stringToken) {
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(stringToken));
		Token token = mongoTemplate.findOne(query, Token.class, "TokenClient");
		return (token != null);
	}
}
