package mg.ituproject.stm.models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DataConsomation extends Data{
	// Fields
	protected String idSousForfait;
	protected BigDecimal cout;
	
	// Getters & Setters
	public String getIdSousForfait() {
		return idSousForfait;
	}
	public void setIdSousForfait(String idSousForfait) {
		this.idSousForfait = idSousForfait;
	}
	public BigDecimal getCout() {
		return cout;
	}
	public void setCout(BigDecimal cout) {
		this.cout = cout;
	}
	
	public Consomation consomer(Connection connection, Consomation consomation) throws SQLException {
		if(this.getData().doubleValue() == 0)
			return consomation;
		
		if(this.getDateExpiration() != null && this.dateExpiration.before(new Date())) {
			delete(connection);
			return consomation;
		}
		
		double value =  this.getData().doubleValue() - (consomation.getQuantite() * this.getCout().doubleValue());
		System.out.println(value);
		if (value <= 0) {
			consomation.setQuantite((int)Math.abs(value));
			this.setData(new BigDecimal(0));
			
		}
		else {
			consomation.setQuantite(0);
			this.setData(new BigDecimal(value));
		}
		this.update(connection);
		return consomation;
	}
	
	public void delete(Connection connection) throws SQLException {
		try(PreparedStatement pstmt = connection.prepareStatement("DELETE FROM DATA WHERE IDCLIENT = ? AND IDSOUSOFFRE = ?")){
			connection.setAutoCommit(false);
			pstmt.setString(1, this.getIdClient());
			pstmt.setString(2, idSousOffre);
			pstmt.addBatch();
			pstmt.executeUpdate();
			connection.commit();
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex.getNextException();
		}
	}
	
	public void update(Connection connection) throws SQLException {
		try(PreparedStatement pstmt = connection.prepareStatement("UPDATE DATA SET DATA=? WHERE IDCLIENT=? AND IDSOUSOFFRE=? AND IDFORFAIT=?")){
			connection.setAutoCommit(false);
			pstmt.setBigDecimal(1, this.getData());
			pstmt.setString(2, this.getIdClient());
			pstmt.setString(3, this.getIdSousOffre());
			pstmt.setString(4, this.getIdForfait());
			pstmt.addBatch();
			System.out.print(pstmt);
			pstmt.executeBatch();
			connection.commit();
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex.getNextException();
		}
	}
}
