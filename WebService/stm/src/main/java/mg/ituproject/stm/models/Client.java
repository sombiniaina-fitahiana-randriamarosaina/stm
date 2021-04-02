package mg.ituproject.stm.models;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.result.DeleteResult;

import mg.ituproject.stm.utils.DateHelpers;
import mg.ituproject.stm.utils.databases.Database;
import mg.ituproject.stm.utils.databases.DatabaseHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;

public class Client {
	// Fields
	protected String idClient;
	protected String nomClient;
	protected String cin;
	protected String numero;
	protected String motDePasse;
	protected Token token;

	// Constructor
	public Client() {
	}
	public Client(String idClient) {
		this.setIdClient(idClient);
	}
	public Client(Token token) {
		this.token = token;
	}
	public Client(String idClient, Token token) {
		this.setIdClient(idClient);
		this.token = token;
	}

	// Getters
	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}

	public String getMotDePasse() {
		return DigestUtils.sha1Hex(motDePasse);
	}
	
	public void inscription (Connection connection) throws ControlException, SQLException{
		List<Client> lc = new ArrayList<>();
		lc.add(this);
		DatabaseHelper.insert(connection, lc, Database.POSTGRESQL);
	}

	public List<Data> generateDataOf(List<SousOffreComplet> listsousoffre){
		List<Data> datas = new ArrayList<>();
		String idClient = this.getIdClient();
		for (SousOffreComplet sousOffreComplet : listsousoffre) {
			String idSousOffre = sousOffreComplet.getIdSousOffre();
			String idForfait = sousOffreComplet.getIdForfait();
			BigDecimal data = new BigDecimal(sousOffreComplet.getVolume());
			Timestamp dateExpiration = null;
			if(sousOffreComplet.getValidite() == 0)
				dateExpiration = DateHelpers.getFinJournee();
			else
				dateExpiration = new Timestamp(DateHelpers.addDays(new Date() , sousOffreComplet.getValidite()).getTime());
			datas.add(new Data(idSousOffre, idClient, idForfait, data, dateExpiration));
		}
		return datas;
	}
	
	public void updateData(Connection connection, List<SousOffreComplet> listsousoffre,  List<Data> datas) throws SQLException{
		Map<String, SousOffreComplet> map = SousOffreComplet.maperSousOffreComplet(listsousoffre);
		try(PreparedStatement pstmt = connection.prepareStatement("UPDATE DATA SET DATA=?, DATEEXPIRATION=? WHERE IDCLIENT=? AND IDSOUSOFFRE=?")){
			connection.setAutoCommit(false);
			for (Data data : datas) {
				Timestamp exp = new Timestamp(DateHelpers.addDays(new Date() , map.get(data.getIdSousOffre()).getValidite()).getTime());
				pstmt.setBigDecimal(1, data.getData().add(new BigDecimal(map.get(data.getIdSousOffre()).getVolume())));
				pstmt.setTimestamp(2, exp);
				pstmt.setString(3, this.getIdClient());
				pstmt.setString(4, data.getIdSousOffre());
				pstmt.addBatch();
			}
			pstmt.executeUpdate();
			connection.commit();
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex.getNextException();
		}
	}
	
	public Token connexion(Connection connection, MongoTemplate mongoTemplate) throws ControlException, SQLException{
		try {
			String requete = String.format("select * from Client where nomClient='%s'", getNomClient());
            List<Client> lc = DatabaseHelper.find(connection, requete, Client.class);
			if(lc.size() == 0)
            	throw new ControlException("Aucun compte n'est attribuer a ce nom!", "nomClient");
            else if (lc.get(0).motDePasse.compareTo(this.getMotDePasse()) != 0)
            	throw new ControlException("le mot de passe est incorrecte", "moteDePasse");
            else {
            	this.setIdClient(lc.get(0).getIdClient());
            	Query queryToken = new Query();
            	queryToken.addCriteria(Criteria.where("idPersonne").is(lc.get(0).getIdClient()));
        		Token token = mongoTemplate.findOne(queryToken, Token.class, "TokenClient");
            	if(token == null) {
            		token = Token.generateToken(this);
            		token.setIdPersonne(lc.get(0).getIdClient());
            		token.insertClient(mongoTemplate);
            	}
            	return token;
            }
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
    }

	public void deconnexion(MongoTemplate mongoTemplate) throws ControlException {
		if(!Token.estConnecteClient(mongoTemplate, this.token.getToken()))
			throw new ControlException("Vous n'etes pas connecter", "token");

		Query query = new Query();
		query.addCriteria(Criteria.where("token").is(this.token.getToken()));
		DeleteResult result = mongoTemplate.remove(query, "TokenClient");
		if(result.getDeletedCount() == 0)
			throw new ControlException("token invalide", "token");
	}

	public void getCompte(Connection connection, MongoTemplate mongoTemplate) throws ControlException, ValidateException, SQLException{
		if(!Token.estConnecteCompteClient(mongoTemplate, this.getIdClient(),this.token.getToken()))
			throw new ControlException("Vous n'etes pas connecter a ce compte", "token");
		try {
			String requete = String.format("select * from Compte where idClient='%s'", getIdClient());
            List<Compte> lc = DatabaseHelper.find(connection, requete, Compte.class);
			if(lc.size() == 0)
            	throw new ControlException("Aucun compte n'est attribuer a cet id!", "idClient");
            else
            	throw new ValidateException("ok", lc.get(0));
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public List<Appel> getHistoriqueAppels(MongoTemplate mongoTemplate) throws ControlException {
		if(!Token.estConnecteCompteClient(mongoTemplate, this.getIdClient(),this.token.getToken()))
			throw new ControlException("Vous n'etes pas connecter a ce compte", "token");
		
		Query query = new Query();
		query.addCriteria(Criteria.where("idClient").is(this.getIdClient()));
		return mongoTemplate.find(query, Appel.class, "Appel");
	}
	
	public void AchatCredit(Connection connection, Double montant) throws SQLException {
		try(PreparedStatement pstmt = connection.prepareStatement("UPDATE DATA SET DATA=? WHERE IDCLIENT=? AND IDSOUSOFFRE='DEFAUT'")){
			connection.setAutoCommit(false);
			pstmt.setBigDecimal(1, new BigDecimal(montant));
			pstmt.setString(2, this.getIdClient());
			pstmt.executeUpdate();
			connection.commit();
		}
		catch(SQLException ex) {
			connection.rollback();
			throw ex.getNextException();
		}
	}
	
	public void controlAchatOffre(Connection connection, Integer prix) throws InstantiationException, IllegalAccessException, SQLException, ControlException {
//		VERIFIENA OE AMPY VE NY VOLANY
		String selectVolako = "SELECT * FROM DATA WHERE IDCLIENT='%s' AND IDSOUSOFFRE='DEFAUT'";
		selectVolako = String.format(selectVolako, this.getIdClient());
		Data volako = DatabaseHelper.find(connection, selectVolako, Data.class).get(0);
		if(volako.getData().doubleValue() < prix) 
			throw new ControlException("Credit Insuffisant", null);
		else {
			try(PreparedStatement pstmt = connection.prepareStatement("UPDATE DATA SET DATA=? WHERE IDCLIENT=? AND IDSOUSOFFRE='DEFAUT'")){
				connection.setAutoCommit(false);
				pstmt.setBigDecimal(1, new BigDecimal(volako.getData().doubleValue() - prix));
				pstmt.setString(2, this.getIdClient());
				pstmt.executeUpdate();
				connection.commit();
			}
			catch(SQLException ex) {
				connection.rollback();
				throw ex.getNextException();
			}
		}
	}
	
	public void achatOffre (MongoTemplate mongoTemplate, Connection connection, SousOffre sousOffre) throws ControlException, InstantiationException, IllegalAccessException, SQLException {
		if(!Token.estConnecteCompteClient(mongoTemplate, this.getIdClient(),this.token.getToken()))
			throw new ControlException("Vous n'etes pas connecter a ce compte", "token");
		
		String selectSousOffre = "SELECT * FROM SOUSOFFRE NATURAL JOIN VOLUMESOUSOFFRE WHERE IDSOUSOFFRE='%s'";
		selectSousOffre = String.format(selectSousOffre, sousOffre.getIdSousOffre());
		List<SousOffreComplet> listsousoffre = DatabaseHelper.find(connection, selectSousOffre, SousOffreComplet.class);
		
		if(listsousoffre.size() != 0) {
			this.controlAchatOffre(connection, listsousoffre.get(0).getPrix());
			List<AchatOffre> lachatO = new ArrayList<>();
			lachatO.add( new AchatOffre(this.getIdClient(), listsousoffre.get(0).getIdOffre(),new Timestamp(new Date().getTime())));
			DatabaseHelper.insert(connection, lachatO, Database.POSTGRESQL);
		}
		
		String selectData = "SELECT * FROM DATA WHERE IDCLIENT='%s' AND IDSOUSOFFRE='%s'";
		selectData = String.format(selectData, this.getIdClient(), sousOffre.getIdSousOffre());
		List<Data> myDatas = DatabaseHelper.find(connection, selectData, Data.class);
		
		if(myDatas.size() == 0) 
			DatabaseHelper.insert(connection, generateDataOf(listsousoffre), Database.POSTGRESQL);
		else 
			this.updateData(connection, listsousoffre, myDatas);
		
//		insert anaty achat offre
	}
	
	public void consomer(MongoTemplate mongoTemplate, Connection connection, Consomation consomation) throws ControlException, InstantiationException, IllegalAccessException, SQLException {
		if(!Token.estConnecteCompteClient(mongoTemplate, this.getIdClient(),this.token.getToken()))
			throw new ControlException("Vous n'etes pas connecter a ce compte", "token");
		String selectMyData = "SELECT * FROM (SELECT * FROM CONSOOFFRE UNION SELECT * FROM CONSOCREDIT) R WHERE IDCLIENT = '%s' AND IDSOUSFORFAIT='%s' ORDER BY DATEEXPIRATION";
		selectMyData = String.format(selectMyData, this.getIdClient(), consomation.getIdSousForfait());
		System.out.println(selectMyData);
		List<DataConsomation> myData = DatabaseHelper.find(connection, selectMyData, DataConsomation.class);
		for (DataConsomation dataConsomation : myData) { 
			consomation = dataConsomation.consomer(connection, consomation);
			if(consomation.getQuantite() == 0)
				return;
		}
		throw new ControlException("Offre et Credit epuise!", null);
	}
}
