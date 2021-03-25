package mg.ituproject.stm.models;

import java.util.Date;

public class Historique {
	// Fields
	protected String idClient;
	protected String numero;
	protected int nombre;
	protected Date date;
	
	// Getters & Setters
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
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
