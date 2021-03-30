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
	protected Timestamp dateAppel;
	protected Integer type;
	protected Integer idForfait;
	
	
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
	public int tarifAUtiliser() {
		int n = 0;
		if(this.getType()==0) {
			n=0;
		}
		if(this.getType()<0) {
			return 1;
		}
		if(this.getType()>0) {
			return 2;
		}
		return n;
	}
	public double calculCout(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		System.out.println("fafafafafa"+tarifAUtiliser());
		return this.getDuree()* ((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	} 
	public double tempMax(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		
		return data.getData().doubleValue()/((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	
	}
	public ArrayList<Data> getAllData(Connection connection) throws InstantiationException, IllegalAccessException, SQLException{
		String requete=String.format("select data.*,prixoffre.cout from data  join prixoffre on (data.idoffre=prixoffre.idoffre and data.idforfait=prixoffre.idforfait) where idClient='%s' and (dateexpiration>now() or data.idoffre='DEFAUT') and data.idforfait=%d and data>0 ",this.getIdClient(),this.getIdForfait());
		System.out.println(requete);
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
    public void control(Connection connection,Data data)throws ControlException,ValidateException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		if(this.getDuree()<0){
			throw new ControlException ("duree invalide","");
		}
		if(this.getDuree()>tempMax(data)) {
			double duree=this.getDuree();
			this.setDuree((int)tempMax(data));
			double restetemp=duree-tempMax(data);	
			throw new ValidateException("Votre offre est epuisee",restetemp);
		}
		throw new ValidateException("ok",null);	
	}
	public void insert(MongoTemplate mongoTemplate) 
	{
		//insert into monogoDB
	}
	
}
