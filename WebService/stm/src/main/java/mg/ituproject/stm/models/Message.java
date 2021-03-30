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

public class Message {
	protected String idMessage;
	protected String idClient;
	protected String numero;
	protected Timestamp dateMessage;
	public String getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(String idMessage) {
		this.idMessage = idMessage;
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
	public Timestamp getDateMessage() {
		return dateMessage;
	}
	public void setDateMessage(Timestamp dateMessage) {
		this.dateMessage = dateMessage;
	}
	protected Integer idForfait;
	
	public void setIdForfait(Integer forf) {
		this.idForfait=forf;
	}
	public Integer getIdForfait() {
		return this.idForfait;
	}
	public Message(String idMessage, String idClient, String numero, Timestamp dateMessage) {
		super();
		this.idMessage = idMessage;
		this.idClient = idClient;
		this.numero = numero;
		this.dateMessage = dateMessage;
	}
	public Message() {
		super();
	}
	public int tarifAUtiliser() {
		//verifier par numero
		return 0;
	}
	public double calculCout(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		return ((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	} 
	public double MessageMax(Data data) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, SQLException {
		return data.getData().doubleValue()/((BigDecimal)Array.get(data.getCout().getArray(),tarifAUtiliser())).doubleValue();
	}
public void control(Connection connection,Data data)throws ControlException,ValidateException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
	
		if(MessageMax(data)<1) {
			throw new ControlException ("non envoyee","");
		}
		throw new ValidateException("ok",null);
	}
	public void insert(Connection connection) 
	{
		try 
		{
			List<Message>connexion =new ArrayList<Message>();
			connexion.add(this);
			DatabaseHelper.insert(connection,connexion,Database.POSTGRESQL);
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	public ArrayList<Data> getAllData(Connection connection) throws InstantiationException, IllegalAccessException, SQLException{
		String requete=String.format("select data.*,prixoffre.cout from data  join prixoffre on (data.idoffre=prixoffre.idoffre and data.idforfait=prixoffre.idforfait) where idClient='%s' and (dateexpiration>now() or data.idoffre='DEFAUT') and data.idforfait=%d and data>0 ",this.getIdClient(),this.getIdForfait());
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    ArrayList<Data> listData=new ArrayList<Data>();
	    try{
      
            stmt = connection.prepareStatement(requete);
          
           
            rs = stmt.executeQuery();
            while(rs.next()) {
            	listData.add(new Data(rs.getString("idClient"),rs.getString("idOffre"),rs.getInt("idForfait"),rs.getBigDecimal("data"),rs.getTimestamp("dateExpiration"),rs.getArray("cout")));
            	System.out.println("aaaaa"+Array.get(listData.get(0).getCout().getArray(),0));
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
