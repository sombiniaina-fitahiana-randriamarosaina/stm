package mg.ituproject.stm.controllers;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Admin;
import mg.ituproject.stm.models.Appel;
import mg.ituproject.stm.models.Client;
import mg.ituproject.stm.models.Compte;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.models.Connexion;
import mg.ituproject.stm.models.Message;
import mg.ituproject.stm.models.Transaction;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.*;
import mg.ituproject.stm.utils.webServices.WebServiceObject;


@RestController
@RequestMapping("/client")
public class ClientController {
	@Autowired
	MongoTemplate mongoTemplate;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/inscription")
	public WebServiceObject connectinscriptionion(@RequestBody Client client)
	{
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			client.inscription(connection);
			Token token = client.connexion(connection, mongoTemplate);
			return new WebServiceObject(200, "Inscription Reussie", token);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/connexion")
	public WebServiceObject connection(@RequestBody Client client) {
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			Token token = client.connexion(connection, mongoTemplate);
			return new WebServiceObject(200, "Connexion Reussie", token);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/deconnexion")
	public WebServiceObject deconnection(HttpServletRequest request) {
		try {
			String token = Token.extract(request);
			Client client = new Client(new Token(null, token));
			client.deconnexion(mongoTemplate);
			return new WebServiceObject(200, "ok", null);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/message")
	@ResponseBody
	public WebServiceObject messenger(@RequestBody Message message) throws InstantiationException, IllegalAccessException
	{

		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			Client client = new Client(message.getIdClient());
			client.Message(connection, mongoTemplate, message);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		catch(ValidateException ex) {
			return new WebServiceObject(200, ex.getMessage(), ex.getData());
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
		return null;
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/appeler")
	@ResponseBody
	public WebServiceObject appeller(@RequestBody Appel appel) throws InstantiationException, IllegalAccessException
	{

		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			Client client = new Client(appel.getIdClient());
			client.appeller(connection, mongoTemplate, appel);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		catch(ValidateException ex) {
			return new WebServiceObject(200, ex.getMessage(), ex.getData());
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
		return null;
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/internet")
	@ResponseBody
	public WebServiceObject connecter(@RequestBody Connexion connexion) throws InstantiationException, IllegalAccessException
	{

		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			Client client = new Client(connexion.getIdClient());
			client.connecter(connection, mongoTemplate, connexion);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		catch(ValidateException ex) {
			return new WebServiceObject(200, ex.getMessage(), ex.getData());
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
		return null;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/comptes/{idClient}")
	public WebServiceObject getCompte(@PathVariable("idClient") String idClient){
		String user = "admin";
		Compte compte = null;
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			// mila maka token
			Client client = new Client(idClient);
			client.getCompte(connection);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		catch(ValidateException ex) {
			return new WebServiceObject(200, ex.getMessage(), ex.getData());

		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
		return null;
	}
}
