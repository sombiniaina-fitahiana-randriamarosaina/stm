package mg.ituproject.stm.models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import mg.ituproject.stm.utils.DateHelpers;


public class Data {
	public Data(String idOffre, String idClient, String idForfait, BigDecimal data, Timestamp dateExpiration) {
		super();
		this.idSousOffre = idOffre;
		this.idClient = idClient;
		this.idForfait = idForfait;
		this.data = data;
		this.dateExpiration = dateExpiration;
	}
	
	public Data() {
		super();
	}
	// Fields
	protected String idSousOffre;
	protected String idClient;
	protected String idForfait;
	protected BigDecimal data;
	protected Timestamp dateExpiration;
	
	// Setters & Getters
	public String getIdSousOffre() {
		return idSousOffre;
	}
	public void setIdSousOffre(String idOffre) {
		this.idSousOffre = idOffre;
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getIdForfait() {
		return idForfait;
	}
	public void setIdForfait(String idForfait) {
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
}
