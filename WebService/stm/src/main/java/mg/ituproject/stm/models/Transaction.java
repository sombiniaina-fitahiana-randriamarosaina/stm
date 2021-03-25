package mg.ituproject.stm.models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Transaction {
	// Fields
	protected String idTransaction;
	protected String idClient;
	protected BigDecimal montant;
	protected Timestamp dateTransaction;
	protected boolean etat;
	
	// Getters & Setters
	public String getIdTransaction() {
		return idTransaction;
	}
	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public BigDecimal getMontant() {
		return montant;
	}
	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}
	public Timestamp getDateTransaction() {
		return dateTransaction;
	}
	public void setDateTransaction(Timestamp dateTransaction) {
		this.dateTransaction = dateTransaction;
	}
	public boolean isEtat() {
		return etat;
	}
	public void setEtat(boolean etat) {
		this.etat = etat;
	}
	
	public static List<Transaction> findDepotsNonValider(Connection connection){
		List<Transaction> lc = null;
		try {
			String requete = String.format("SELECT * FROM DEPOTNONVALIDER");	        
			lc = DatabaseHelper.find(connection, requete, Transaction.class);
		} catch (InstantiationException | SQLException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return lc;
	}
}
