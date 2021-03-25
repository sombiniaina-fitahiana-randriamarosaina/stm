package mg.ituproject.stm.models;

import org.bson.Document;

import mg.ituproject.stm.utils.databases.MongoDocument;

public class ForfaitAppel implements MongoDocument{
	// Fields
	protected double memeOperateur;
	protected double autreOperateur;
	protected double international;
	
	// Getters & Setter
	public double getMemeOperateur() {
		return memeOperateur;
	}
	public void setMemeOperateur(double memeOperateur) {
		this.memeOperateur = memeOperateur;
	}
	public double getAutreOperateur() {
		return autreOperateur;
	}
	public void setAutreOperateur(double autreOperateur) {
		this.autreOperateur = autreOperateur;
	}
	public double getInternational() {
		return international;
	}
	public void setInternational(double international) {
		this.international = international;
	}
	
	@Override
	public Document toDocument() {
		Document doc = new Document("memeOperateur", this.getMemeOperateur())
              .append("autreOperateur", this.getAutreOperateur())
              .append("international", this.getInternational());
		return doc;
	}
	
	
}
