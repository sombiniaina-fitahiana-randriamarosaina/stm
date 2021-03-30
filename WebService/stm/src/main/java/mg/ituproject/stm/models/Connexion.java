package mg.ituproject.stm.models;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Connexion {
	protected String idConnexion;
	protected String idClient;
	protected Integer volume;
	protected Timestamp date;
	protected Integer idForfait;
	
	public void setIdForfait(Integer forf) {
		this.idForfait=forf;
	}
	public Integer getIdForfait() {
		return this.idForfait;
	}
	
	public String getIdClient() {
		return idClient;
	}
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public Integer getVolume() {
		return volume;
	}
	public void setVolume(Integer volume) {
		this.volume = volume;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	
	public Connexion() {
		super();
	}
	public int tarifAUtiliser() {
		//verifier par numero
		return 1;
	}
	
	public Connexion(String idConnexion, String idClient, Integer volume, Timestamp date, Integer idForfait) {
		super();
		this.idConnexion = idConnexion;
		this.idClient = idClient;
		this.volume = volume;
		this.date = date;
		this.idForfait = idForfait;
	}
	public double calculCout(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		return this.getVolume()* ((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	} 
	public double VolumeMax(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		return data.getData().doubleValue()/((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	}
public void control(Connection connection,Data data)throws ControlException,ValidateException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		if(this.getVolume()<0){
			throw new ControlException ("duree invalide","");
		}
		
		if(this.getVolume()>VolumeMax(data)) {
			this.setVolume((int)VolumeMax(data));
			double resteVol=this.getVolume()-VolumeMax(data);
			throw new ValidateException("Votre offre est epuisee",resteVol);
		}
		throw new ValidateException("ok",null);	
	}
	public void insert(Connection connection) 
	{
		try 
		{
			List<Connexion>connexion =new ArrayList<Connexion>();
			connexion.add(this);
			DatabaseHelper.insert(connection,connexion,Database.POSTGRESQL);
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Data> getAllData(Connection connection) throws InstantiationException, IllegalAccessException, SQLException{
		String requete=String.format("select data.*,prixoffre.cout from data  join prixoffre on (data.idoffre=prixoffre.idoffre and data.idforfait=prixoffre.idforfait) where idClient='%s' and (dateexpiration>now() or data.idoffre='DEFAUT') and data.idforfait=%d ",this.getIdClient(),this.getIdForfait());
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<Data> listData=new ArrayList<Data>();
	    try{
      
            stmt = connection.prepareStatement(requete);
          
           
            rs = stmt.executeQuery();
            while(rs.next()) {
            	listData.add(new Data(rs.getString("idClient"),rs.getString("idOffre"),rs.getInt("idForfait"),rs.getBigDecimal("data"),rs.getTimestamp("dateExpiration"),rs.getArray("cout")));
         
            }
	    }
        catch(Exception e){
             throw e;
        }
	    rs.close();
	    stmt.close(); 
		return listData;
	}
	
}
