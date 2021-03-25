package mg.ituproject.stm.models;

public class Statistiques {
	public Statistiques() {
		
	}
	// Fields
	protected String idOffre;
	protected String nomOffre;
	protected String libele;
	protected int nombre;
	
	// Getters & Setters
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
	}
	public String getNomOffre() {
		return nomOffre;
	}
	public void setNomOffre(String nomOffre) {
		this.nomOffre = nomOffre;
	}
	public String getLibele() {
		return libele;
	}
	public void setLibele(String libele) {
		this.libele = libele;
	}
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}	
}
