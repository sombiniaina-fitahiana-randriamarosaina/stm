package mg.ituproject.stm.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mg.ituproject.stm.utils.databases.DatabaseHelper;

public class PrixOffre {
	// Fields
	protected String idOffre;
	protected int idForfait;
	protected int prix;
	protected int validite;
	protected BigDecimal[] cout;
	
	// Getters & Setters
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public Integer getIdForfait() {
		return idForfait;
	}
	public void setIdForfait(int idForfait) {
		this.idForfait = idForfait;
	}
	public int getPrix() {
		return prix;
	}
	public void setPrix(int prix) {
		this.prix = prix;
	}
	public Integer getValidite() {
		return validite;
	}
	public void setValidite(int validite) {
		this.validite = validite;
	}
	public BigDecimal[] getCout() {
		return cout;
	}
	public void setCout(BigDecimal[] cout) {
		this.cout = cout;
	}

	public static void insertAll(Connection connection, List<PrixOffre> list, boolean autocommit) throws SQLException{
		String sqlInsertPrixOffre = "INSERT INTO PRIXOFFRE (IDOFFRE, IDFORFAIT, PRIX, VALIDITE, COUT) VALUES (?, ?, ?, ?, ?)";
		try(PreparedStatement insertPrixOffre = connection.prepareStatement(sqlInsertPrixOffre)){
			connection.setAutoCommit(autocommit);   
			for (PrixOffre po : list) {
        		insertPrixOffre.setString(1, po.getIdOffre());
        		insertPrixOffre.setInt(2, po.getIdForfait());
        		insertPrixOffre.setInt(3, po.getPrix());
        		insertPrixOffre.setInt(4, po.getValidite());
        		insertPrixOffre.setArray(5, connection.createArrayOf("decimal", po.getCout()));
        		insertPrixOffre.addBatch();
            }
			insertPrixOffre.executeBatch();
			if(autocommit) 
			  connection.commit();
		}
		catch (SQLException e) {
			connection.rollback();
			throw e.getNextException();
		}
	}
}
