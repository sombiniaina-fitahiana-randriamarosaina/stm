package mg.ituproject.stm.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Offre;
import mg.ituproject.stm.models.Statistiques;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("admin/statistiques")
public class StatistiquesController {
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "offres/{idOffre}/jour")
	public WebServiceObject getStatOffreJournalier(HttpServletRequest request, @PathVariable("idOffre") String idOffre){
		try (Connection connection = ConnectionHelper.getConnection()){
			Offre offre = new Offre(idOffre);
			String token = Token.extract(request);
			List<Statistiques> statistiques = offre.getStatistiqueJournalier(connection, mongoTemplate, token);
			return new WebServiceObject(200, "ok", statistiques);
		}
		catch (ControlException e) {
			return new WebServiceObject(500, e.getMessage(), null);
		} catch (SQLException | ClassNotFoundException e1) {
			return new WebServiceObject(500, e1.getMessage(), null);
		} 
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "offres/{idOffre}/mois")
	public WebServiceObject getStatOffreMensuel(HttpServletRequest request, @PathVariable("idOffre") String idOffre){
		try (Connection connection = ConnectionHelper.getConnection()){
			Offre offre = new Offre(idOffre);
			String token = Token.extract(request);
			List<Statistiques> statistiques = offre.getStatistiqueMensuel(connection, mongoTemplate, token);
			return new WebServiceObject(200, "ok", statistiques);
		}
		catch (ControlException e) {
			return new WebServiceObject(500, e.getMessage(), null);
		} catch (SQLException | ClassNotFoundException e1) {
			return new WebServiceObject(500, e1.getMessage(), null);
		} 
	}
}
