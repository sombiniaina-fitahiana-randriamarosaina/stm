package mg.ituproject.stm.models;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import mg.ituproject.stm.utils.databases.DatabaseHelper;

public class Data {
	String idClient;
	String idOffre;
	int idForfait;
	BigDecimal data;
	Timestamp dateExpiration;
	Array cout;
	
	public Data() {
		super();
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public int getIdForfait() {
		return idForfait;
	}
	public void setIdForfait(int idForfait) {
		this.idForfait = idForfait;
	}
	public BigDecimal getData() {
		return data;
	}
	public void setData(BigDecimal data) {
		this.data = data;
	}
	public Timestamp getDateExpiration() {
		return dateExpiration;
	}
	public void setDateExpiration(Timestamp dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	public void setCout(Array cout) {
		this.cout=cout;
		}
	public Array getCout() {
		return this.cout;
	}
	public Data(String idClient, String idOffre, int idForfait, BigDecimal data, Timestamp dateExpiration,
			Array cout) {
		super();
		this.idClient = idClient;
		this.idOffre = idOffre;
		this.idForfait = idForfait;
		this.data = data;
		this.dateExpiration = dateExpiration;
		this.cout = cout;
	}
	public void UpdateData(Connection connection,double cout) throws SQLException {
		
			String requete=String.format("update data set data=data-%d where idOffre='%s'", (int)cout ,this.getIdOffre());
			System.out.println(requete);
			PreparedStatement stmt = null;
			try {
			
	            stmt = connection.prepareStatement(requete);
	            stmt.executeUpdate();
	            connection.commit();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				  
	            stmt.close();
			}
			
         
    
	}
	
	
	
	
	
	
}
