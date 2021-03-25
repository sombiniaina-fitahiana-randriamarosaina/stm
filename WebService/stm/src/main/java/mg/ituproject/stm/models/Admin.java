package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.databases.MongoHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Admin {
	// Fields
	protected String idAdmin;
	protected String pseudo;
	protected String motDePasse;
	
	// Getters & Setters
	public String getIdAdmin() {
		return idAdmin;
	}
	public void setIdAdmin(String idAdmin) {
		this.idAdmin = idAdmin;
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

	public void connexion(MongoHelper mongoHelper) throws ControlException, ValidateException{
		Document admin = mongoHelper.find("Admin", new Document("pseudo", getPseudo()));
		if(admin == null)
        	throw new ControlException("Aucun Admin n'est attribuer a ce pseudo!", "pseudo");
		else if(admin.getString("motDePasse").compareTo(this.getMotDePasse()) != 0) 
			throw new ControlException("mot de passe incorrecte", "motDePasse");
        else {
        	Token token = Token.generateToken(this);
//        	token.insert(mongoHelper);
        	throw new ValidateException("Connexion Reussie", token);
        }
    }
	
	public static void validerDepot(Connection connection, Transaction transaction) throws ValidateException, ControlException, SQLException{
		// avadika true le etat anle transaction
		throw new ValidateException("Depot valider", null);
	}
}
