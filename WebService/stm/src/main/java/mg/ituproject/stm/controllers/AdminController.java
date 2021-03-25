package mg.ituproject.stm.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Admin;
import mg.ituproject.stm.models.Client;
import mg.ituproject.stm.models.Offre;
import mg.ituproject.stm.models.Transaction;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.databases.MongoHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/connexion")
	public WebServiceObject connection(@RequestBody Admin admin) 
	{
		Connection connection = null;
		MongoHelper mongoHelper = null;
		try {
			mongoHelper = ConnectionHelper.connectMongoDB();
			admin.connexion(mongoHelper);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
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
