package mg.ituproject.stm.models;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Appel {
	// Fields
	protected String idAppel;
	protected String idClient;
	protected String numero;
	protected Integer duree;
	protected Date dateAppel;
	protected Integer type;
	protected Integer idForfait = 2;
	
	
	// Getters & Setters
	public String getIdAppel() {
		return idAppel;
	}
	public void setIdAppel(String idAppel) {
		this.idAppel = idAppel;
	}
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Integer getDuree() {
		return duree;
	}
	public void setDuree(Integer duree) {
		this.duree = duree;
	}
	public Date getDateAppel() {
		return dateAppel;
	}
	public void setDateAppel(Date dateAppel) {
		this.dateAppel = dateAppel;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public void setIdForfait(Integer forf) {
		this.idForfait=forf;
	}
	public Integer getIdForfait() {
		return this.idForfait;
	}
	
}
