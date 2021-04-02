package mg.ituproject.stm.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

import mg.ituproject.stm.models.Compte;
import mg.ituproject.stm.models.Offre;
import mg.ituproject.stm.models.SousOffre;
import mg.ituproject.stm.models.Statistiques;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.models.VolumeSousOffre;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/offres")
public class OffreController {
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@ResponseBody
	@PostMapping("/")
	public WebServiceObject ajoutOffre(@RequestBody Offre offre){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			offre.insert(connection);
			return new WebServiceObject(200, "Insertion Offre Reussie", null);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}
	@CrossOrigin(origins = "http://localhost:4200")
	@ResponseBody
	@PostMapping("/sous-offre")
	public WebServiceObject ajoutSousOffre(@RequestBody SousOffre sousoffre){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			sousoffre.insert(connection);
			return new WebServiceObject(200, "Insertion Sous Offre Reussie", null);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@ResponseBody
	@PostMapping("/volume-sous-offre")
	public WebServiceObject ajoutVolumeSousOffre(@RequestBody List<VolumeSousOffre> list){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			VolumeSousOffre.insertAll(connection, list);
			return new WebServiceObject(200, "Insertion Volume Sous Offre Reussie", null);
		}
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/")
	public WebServiceObject getStatOffreJournalier(HttpServletRequest request){
		try (Connection connection = ConnectionHelper.getConnection()){
			String token = Token.extract(request);
			List<Offre> lo = Offre.findAll(connection);
			return new WebServiceObject(200, "ok", lo);
		}
		catch(SQLException | ClassNotFoundException | ControlException | InstantiationException | IllegalAccessException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
	}
}
