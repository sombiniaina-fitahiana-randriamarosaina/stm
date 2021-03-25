package mg.ituproject.stm.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/offres")
public class OffreController {
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/")
	@ResponseBody
	public WebServiceObject ajoutOffre(@RequestBody Offre offre){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			offre.insert(connection);
			return new WebServiceObject(200, "Insertion Offre Reussie", null);
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
	@GetMapping(value = "/")
	public WebServiceObject findAll()
	{
		Compte compte = null;
		Connection connection = null;
		try {
			ConnectionHelper.connectMongoDB();
			connection = ConnectionHelper.getConnection();
			Offre.findAll(connection);
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
