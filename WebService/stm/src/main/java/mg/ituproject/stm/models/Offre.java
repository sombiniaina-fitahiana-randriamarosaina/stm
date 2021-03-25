package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.origin.SystemEnvironmentOrigin;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Offre{
	public Offre(String idOffre) {
		this.setIdOffre(idOffre);
	}
	public Offre() {
		
	}
	
	// Field
	protected String idOffre;
	protected String nomOffre;
	protected String Description;
	protected List<Horaire> horaires;
	protected List<PrixOffre> prixOffre;
	
	// Set & Get
	public String getIdOffre() {
		return idOffre;
	}
	public void setIdOffre(String idOffre) {
		this.idOffre = idOffre;
		if(horaires != null)
			for (Horaire horaire : horaires)
				horaire.setIdOfffre(idOffre);
		if(prixOffre != null)
			for (PrixOffre pf : prixOffre)
				pf.setIdOffre(idOffre);
	}
	public String getNomOffre() {
		return nomOffre;
	}
	public void setNomOffre(String nomOffre) {
		this.nomOffre = nomOffre;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public List<Horaire> getHoraires() {
		return horaires;
	}
	public void setHoraires(List<Horaire> horaires) {
		this.horaires = horaires;
	}
	public List<PrixOffre> getPrixOffre() {
		return prixOffre;
	}
	public void setPrixOffre(List<PrixOffre> prixOffre) {
		this.prixOffre = prixOffre;
	}
	public List<Horaire> getHoraire(Connection connection) throws SQLException{
		String sql = " SELECT * FROM HORAIRE WHERE IDOFFRE = '%s'";
		sql = String.format(sql, this.getIdOffre());
		List<Horaire> lh = null;
		try {
			lh = DatabaseHelper.find(connection, sql, Horaire.class);
		} catch (InstantiationException | IllegalAccessException e) {
			
		}
		return lh;
	}
	
	public void insert(Connection connection) throws ControlException{
		List<Offre> lo = new ArrayList<>();
		lo.add(this);
		String sqlInsertOffre = "INSERT INTO OFFRE (NOMOFFRE, DESCRIPTION) VALUES (?, ?)";
        try (PreparedStatement insertOffre = connection.prepareStatement(sqlInsertOffre, Statement.RETURN_GENERATED_KEYS)){
        	connection.setAutoCommit(false);
        	insertOffre.setString(1, this.getNomOffre());
        	insertOffre.setString(2, this.getDescription());
        	insertOffre.addBatch();
        	insertOffre.executeBatch();
        	try (ResultSet generatedKeys = insertOffre.getGeneratedKeys()) {
                if (generatedKeys.next())
                    this.setIdOffre(generatedKeys.getString(1));
            }
        	if(horaires != null) Horaire.insertAll(connection, horaires, false);
        	if(prixOffre != null)PrixOffre.insertAll(connection, prixOffre, false);
            connection.commit();
        }
        catch (SQLException e) {
           try {
				connection.rollback();
			} catch (SQLException e1) {}
        }
	}
	
	public List<Statistiques> getStatistiqueJournalier(Connection connection) throws ControlException{
		try {
			String requette = "SELECT * FROM STATOFFREJOURNALIER WHERE IDOFFRE='%s'";
			requette = String.format(requette, getIdOffre());
			List<Statistiques> ls = DatabaseHelper.find(connection, requette, Statistiques.class);
			return ls;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Statistiques> getStatistiqueMensuel(Connection connection) throws ControlException{
		try {
			String requette = "SELECT * FROM STATOFFREMENSUEL WHERE IDOFFRE='%s'";
			requette = String.format(requette, getIdOffre());
			List<Statistiques> ls = DatabaseHelper.find(connection, requette, Statistiques.class);
			return ls;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void findAll(Connection connection) throws SQLException, ValidateException{
		try {
			String requette = "SELECT * FROM OFFRE WHERE IDOFFRE!='DEFAUT'";
			List<Offre> ls = DatabaseHelper.find(connection, requette, Offre.class);
			throw new ValidateException("ok", ls);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
