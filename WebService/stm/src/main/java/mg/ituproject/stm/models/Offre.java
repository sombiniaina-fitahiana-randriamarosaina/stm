package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.data.mongodb.core.MongoTemplate;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Offre{
	
	
	public Offre(String idOffre) {
		super();
		this.idOffre = idOffre;
	}
	
	public Offre() {
		super();
	}

	// Fields
	protected String idOffre;
	protected String nomOffre;
	
	// Getters & Setters
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public String getNomOffre() {
		return nomOffre;
	}
	public void setNomOffre(String nomOffre) {
		this.nomOffre = nomOffre;
	}
	
	public static List<Offre> findAll(Connection connection) throws InstantiationException, IllegalAccessException, SQLException{
		return DatabaseHelper.find(connection, "SELECT * FROM OFFRE WHERE IDOFFRE != 'DEFAUT'", Offre.class);
	}
	public void insert (Connection connection) throws SQLException {
		List<Offre> list = new ArrayList<>();
		list.add(this);
		DatabaseHelper.insert(connection, list, Database.POSTGRESQL);
	}
	
	public List<Statistiques> getStatistiqueJournalier(Connection connection, MongoTemplate mongoTemplate, String token) throws ControlException{
		if(!Token.estConnecteAdmin(mongoTemplate, token)) 
			throw new ControlException("Token Invalide", "token");
		try {
			String requette = "SELECT * FROM STATOFFREJOURNALIER WHERE IDOFFRE='%s'";
			requette = String.format(requette, getIdOffre());
			List<Statistiques> ls = DatabaseHelper.find(connection, requette, Statistiques.class);
			return ls;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Statistiques> getStatistiqueMensuel(Connection connection, MongoTemplate mongoTemplate, String token) throws ControlException{
		if(!Token.estConnecteAdmin(mongoTemplate, token)) 
			throw new ControlException("Token Invalide", "token");
		try {
			String requette = "SELECT * FROM STATOFFREMENSUEL WHERE IDOFFRE='%s'";
			requette = String.format(requette, getIdOffre());
			List<Statistiques> ls = DatabaseHelper.find(connection, requette, Statistiques.class);
			return ls;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
