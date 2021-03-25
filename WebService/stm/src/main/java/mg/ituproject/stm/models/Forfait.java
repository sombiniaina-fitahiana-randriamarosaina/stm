package mg.ituproject.stm.models;

public class Forfait {
	// Fields
	protected Integer idForfait;
	protected String nom;
	
	// Getters & Setters
	public Integer getIdForfait() {
		return idForfait;
	}
	public void setIdForfait(Integer idForfait) {
		this.idForfait = idForfait;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String toString() {
		return getIdForfait().toString();
	}
}
