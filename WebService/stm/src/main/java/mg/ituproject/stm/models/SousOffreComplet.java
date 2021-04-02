package mg.ituproject.stm.models;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;

public class SousOffreComplet extends SousOffre{
	// Fields
	protected String idSousOffre;
	protected String idForfait;
	protected Integer volume;
	
	// Setters & Getters
	public String getIdSousOffre() {
		return idSousOffre;
	}
	public void setIdSousOffre(String idSousOffre) {
		this.idSousOffre = idSousOffre;
	}
	public String getIdForfait() {
		return idForfait;
	}
	public void setIdForfait(String idForfait) {
		this.idForfait = idForfait;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public static void insertAll(Connection connection, List<VolumeSousOffre> list) throws SQLException {
		DatabaseHelper.insert(connection, list, Database.POSTGRESQL);
	}
	
	public static Map<String, SousOffreComplet> maperSousOffreComplet(List<SousOffreComplet> list){
		Map<String, SousOffreComplet> map = new HashMap<>();
		for (SousOffreComplet sousOffreComplet : list)
			map.put(sousOffreComplet.getIdSousOffre(), sousOffreComplet);
		return map;
	}
}
