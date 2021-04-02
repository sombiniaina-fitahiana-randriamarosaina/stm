package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;

public class SousOffre {
	// Fields
	protected String idSousOffre;
	protected String idOffre;
	protected Integer prix;
	protected Integer validite;
	
	//Setters & Getters
	public String getIdSousOffre() {
		return idSousOffre;
	}
	public void setIdSousOffre(String idSousOffre) {
		this.idSousOffre = idSousOffre;
	}
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public Integer getPrix() {
		return prix;
	}
	public void setPrix(Integer prix) {
		this.prix = prix;
	}
	public Integer getValidite() {
		return validite;
	}
	public void setValidite(Integer validite) {
		this.validite = validite;
	}
	
	public void insert (Connection connection) throws SQLException {
		List<SousOffre> list = new ArrayList<>();
		list.add(this);
		DatabaseHelper.insert(connection, list, Database.POSTGRESQL);
	}
}
