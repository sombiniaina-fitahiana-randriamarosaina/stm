package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.DeleteResult;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Client {
	// Fields
	protected String idClient;
	protected String nomClient;
	protected String cin;
	protected String numero;
	protected String motDePasse;
	protected Token token;
	
	// Constructor
	public Client() {
	}
	public Client(String idClient) {
		this.setIdClient(idClient);
	}
	public Client(Token token) {
		this.token = token;
	}

	// Getters
	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	
	public String getMotDePasse() {
		return DigestUtils.sha1Hex(motDePasse);
	}

	public void inscription (Connection connection) throws ControlException, SQLException{
		List<Client> lc = new ArrayList<>();
		lc.add(this);
		DatabaseHelper.insert(connection, lc, Database.POSTGRESQL);
	}
	
	public Token connexion(Connection connection, MongoTemplate mongoTemplate) throws ControlException, SQLException{
		try {
			String requete = String.format("select * from Client where nomClient='%s'", getNomClient());
            List<Client> lc = DatabaseHelper.find(connection, requete, Client.class);
			if(lc.size() == 0)
            	throw new ControlException("Aucun compte n'est attribuer a ce nom!", "nomClient");
            else if (lc.get(0).motDePasse.compareTo(this.getMotDePasse()) != 0)
            	throw new ControlException("le mot de passe est incorrecte", "moteDePasse");
            else {
            	this.setIdClient(lc.get(0).getIdClient());
            	Query queryToken = new Query();
            	queryToken.addCriteria(Criteria.where("idPersonne").is(lc.get(0).getIdClient()));
        		Token token = mongoTemplate.findOne(queryToken, Token.class, "TokenClient");
            	if(token == null) {
            		token = Token.generateToken(this);
            		token.setIdPersonne(lc.get(0).getIdClient());
            		token.insertClient(mongoTemplate);
            	}
            	return token;
            }
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
    }
	
	public void deconnexion(MongoTemplate mongoTemplate) throws ControlException {
		if(!Token.estConnecteClient(mongoTemplate, this.token.getToken())) 
			throw new ControlException("Vous n'etes pas connecter", "token");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(this.token.getToken()));
		DeleteResult result = mongoTemplate.remove(query, "Token");
		if(result.getDeletedCount() == 0)
			throw new ControlException("token invalide", "token");
	}
	
	public void getCompte(Connection connection) throws ControlException, ValidateException, SQLException{
		try {
			String requete = String.format("select * from Compte where idClient='%s'", getIdClient());	        
            List<Compte> lc = DatabaseHelper.find(connection, requete, Compte.class);
			if(lc.size() == 0)
            	throw new ControlException("Aucun compte n'est attribuer a cet id!", "idClient");
            else 
            	throw new ValidateException("ok", lc.get(0));
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
