package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.List;

public class Horaire {
	// Fields 
	protected String idOfffre;
	protected Time debut;
	protected Time fin;
	
	// Getters & Setters
	public String getIdOfffre() {
		return idOfffre;
	}
	public void setIdOfffre(String idOfffre) {
		this.idOfffre = idOfffre;
	}
	public Time getDebut() {
		return debut;
	}
	public void setDebut(Time debut) {
		this.debut = debut;
	}
	public Time getFin() {
		return fin;
	}
	public void setFin(Time fin) {
		this.fin = fin;
	}
	
	public String toString() {
		return String.format("%s -> %s", getDebut(), getFin()); 
	}
	
	public static void insertAll(Connection connection, List<Horaire> list, boolean autocommit) throws SQLException{
		String sqlInsertHoraire = "INSERT INTO HORAIRE (IDOFFRE, DEBUT, FIN) VALUES (?, ?, ?)";
		try(PreparedStatement insertHoraire = connection.prepareStatement(sqlInsertHoraire)){
			connection.setAutoCommit(autocommit);
			    
			for(Horaire horaire : list) {
				insertHoraire.setString(1, horaire.getIdOfffre());
				insertHoraire.setTime(2, horaire.getDebut());
				insertHoraire.setTime(3, horaire.getFin());
				insertHoraire.addBatch();
			}
			insertHoraire.executeBatch();
			   	
			if(autocommit) 
			  connection.commit();
		}
		catch (SQLException e) {
			connection.rollback();
			throw e.getNextException();
		}
	}
}
