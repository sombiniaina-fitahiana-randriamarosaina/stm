package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.DeleteResult;

import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Admin {
	
	public Admin(Token token) {
		super();
		this.token = token;
	}
	

	public Admin() {
	}


	// Fields
	@Id
	protected String id;
	
	protected String pseudo;
	protected String motDePasse;
	protected Token token;
	
	// Getters & Setters
	public String getId() {
		return id;
	}
	public void setId(String idAdmin) {
		this.id = idAdmin;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public Token connexion(MongoTemplate mongoTemplate) throws ControlException{
		Query query = new Query();
		query.addCriteria(Criteria.where("pseudo").is(getPseudo()));
		Admin admin = mongoTemplate.findOne(query, Admin.class, "Admin");
				
		if(admin == null)
        	throw new ControlException("Aucun Admin n'est attribuer a ce pseudo!", "pseudo");
		else if(admin.motDePasse.compareTo(getMotDePasse()) != 0) 
			throw new ControlException("mot de passe incorrecte", "motDePasse");
        else {
        	Query queryToken = new Query();
        	queryToken.addCriteria(Criteria.where("idPersonne").is(admin.getId()));
    		Token token = mongoTemplate.findOne(queryToken, Token.class, "TokenAdmin");
        	if(token == null) {
        		token = Token.generateToken(this);
        		token.setIdPersonne(admin.getId());
        		token.insertAdmin(mongoTemplate);
        	}
        	return token;
        }
    }
	
	public void deconnexion(MongoTemplate mongoTemplate) throws ControlException {
		if(!Token.estConnecteAdmin(mongoTemplate, this.token.getToken())) 
			throw new ControlException("Vous n'etes pas connecter", "token");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(this.token.getToken()));
		DeleteResult result = mongoTemplate.remove(query, "Token");
		if(result.getDeletedCount() == 0)
			throw new ControlException("token invalide", "token");
	}
	
	public static void validerDepot(Connection connection, Transaction transaction) throws ValidateException, ControlException, SQLException{
		// avadika true le etat anle transaction
		throw new ValidateException("Depot valider", null);
	}
}
