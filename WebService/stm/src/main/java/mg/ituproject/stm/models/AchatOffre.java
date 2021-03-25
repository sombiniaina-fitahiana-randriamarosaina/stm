package mg.ituproject.stm.models;

import java.sql.Timestamp;

public class AchatOffre {
	// Fields
	protected String idClient;
	protected String idOffre;
	protected Timestamp dateAchat;
	
	// Getters & Setters
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
	public Timestamp getDateAchat() {
		return dateAchat;
	}
	public void setDateAchat(Timestamp dateAchat) {
		this.dateAchat = dateAchat;
	}
	
	
}
