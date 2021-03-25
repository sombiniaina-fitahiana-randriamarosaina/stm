package mg.ituproject.stm.models;

import java.sql.Timestamp;

public class Appel {
	// Fields
	protected String idAppel;
	protected String idClient;
	protected String numero;
	protected Integer duree;
	protected Timestamp dateAppel;
	protected String type;
	
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
	public Timestamp getDateAppel() {
		return dateAppel;
	}
	public void setDateAppel(Timestamp dateAppel) {
		this.dateAppel = dateAppel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
