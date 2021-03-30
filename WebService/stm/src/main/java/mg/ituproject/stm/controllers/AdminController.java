package mg.ituproject.stm.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Admin;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/connexion")
	public WebServiceObject connection(@RequestBody Admin admin) {
		try {
			Token token = admin.connexion(mongoTemplate);
			return new WebServiceObject(200, "ok", token);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/deconnexion")
	public WebServiceObject deconnection(HttpServletRequest request) {
		try {
			String token = Token.extract(request);
			Admin admin = new Admin(new Token(null, token));
			admin.deconnexion(mongoTemplate);
			return new WebServiceObject(200, "ok", null);
		}
		catch(ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
	}
	
	
}
