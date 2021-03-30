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
		DeleteResult result = mongoTemplate.remove(query, "TokenClient");
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
	public void Message(Connection connection, MongoTemplate mongoTemplate,Message message) throws ControlException, ClassNotFoundException, InstantiationException, IllegalAccessException, ValidateException, SQLException {
		ArrayList<Data> listeData=message.getAllData(connection);
		int i=0;
		try{
			if(listeData.size()==0) {
				throw new ControlException("votre offre sms est epuisee","");
			}
			message.control(connection, listeData.get(i));
		}
		catch(ControlException e) {
			throw new ControlException("Message  non envoyee ", e.getMessage());
		}
		catch(ValidateException e) {

//				message.insert(mongoTemplate);
				listeData.get(i).UpdateData(connection,message.calculCout(listeData.get(i)));
				throw new ValidateException("appel effectue", null);
			}
	}
	public void connecter(Connection connection, MongoTemplate mongoTemplate,Connexion connexion) throws ControlException, ClassNotFoundException, InstantiationException, IllegalAccessException, ValidateException, SQLException {
		ArrayList<Data> listeData= connexion.getAllData(connection);
		int i=0;
		try{
			if(listeData.size()==0) {
				throw new ControlException("votre forfait internet est epuisee","");
			}
			connexion.control(connection, listeData.get(i));
		}
		catch(ControlException e) {
			if(e.getMessage().equals("volume invalide")) {
				throw new ControlException("Connexion non effectuee ", e.getMessage());
			}
			else {
				connexion.insert(connection);
				listeData.get(i).UpdateData(connection,connexion.calculCout(listeData.get(i)));
				throw new ValidateException("connexion effectuee...", null);
			}
		}
		catch(ValidateException e) {
			if(e.getMessage().equals("Votre offre est epuisee")) {
				connexion.insert(connection);
				listeData.get(i).UpdateData(connection,connexion.calculCout(listeData.get(i)));
				i++;
				if(i<listeData.size()){
					connexion.setVolume(((Double)e.getData()).intValue());
//					this.connecter(connection, connexion);
				}
				else {
					return;
				}
			}
			else {
				connexion.insert(connection);
				listeData.get(i).UpdateData(connection,connexion.calculCout(listeData.get(i)));
				throw new ValidateException("appel effectue", null);
			}
		}
	}
	public void appeller(Connection connection, MongoTemplate mongoTemplate,Appel appel) throws InstantiationException, IllegalAccessException, SQLException, ControlException,ValidateException, ClassNotFoundException{
		ArrayList<Data> listeData=appel.getAllData(connection);
		int i=0;
		try{
			if(listeData.size()==0) {
				throw new ControlException("votre credit est insufisant","");
			}
			appel.control(connection, listeData.get(i));
		}
		catch(ControlException e) {

				throw new ControlException("appell non effectuee ", e.getMessage());
		}
		catch(ValidateException e) {
			if(e.getMessage().equals("Votre offre est epuisee")) {
				//appel.insert(mongoHelper);
				listeData.get(i).UpdateData(connection,appel.calculCout(listeData.get(i)));
				i++;
				if(i<listeData.size()){
					appel.setDuree(((Double)e.getData()).intValue());
					this.appeller(connection, mongoTemplate, appel);
				}
				else {
					throw new ControlException("Votre offre est epuisee","");
				}
			}
			else {
//				appel.insert(connection);
				listeData.get(i).UpdateData(connection,appel.calculCout(listeData.get(i)));
				throw new ValidateException("appel effectue", null);
			}
		}
	}
}
