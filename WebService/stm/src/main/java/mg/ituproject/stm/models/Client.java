package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.databases.MongoHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Client {
	// Fields
	protected String idClient;
	protected String nomClient;
	protected String cin;
	protected String numero;
	protected String motDePasse;
	
	// Constructor
	public Client() {
	}
	public Client(String idClient) {
		this.setIdClient(idClient);
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

	public void inscription (Connection connection) throws ControlException, ValidateException, SQLException{
		List<Client> lc = new ArrayList<>();
		lc.add(this);
		DatabaseHelper.insert(connection, lc, Database.POSTGRESQL);
		throw new ValidateException("Inscription Reussie", null);
	}
	
	public void connexion(Connection connection, MongoHelper mongoHelper) throws ControlException, ValidateException, SQLException{
		try {
			String requete = String.format("select * from Client where nomClient='%s'", getNomClient());
            List<Client> lc = DatabaseHelper.find(connection, requete, Client.class);
			if(lc.size() == 0)
            	throw new ControlException("Aucun compte n'est attribuer a ce nom!", "nomClient");
            else if (lc.get(0).motDePasse.compareTo(this.getMotDePasse()) != 0)
            	throw new ControlException("le mot de passe est incorrecte", "moteDePasse");
            else {
            	Token token = Token.generateToken(lc.get(0));
//            	token.insert(mongoHelper);
            	throw new ValidateException("Connexion Reussie", token);
            }
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
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
