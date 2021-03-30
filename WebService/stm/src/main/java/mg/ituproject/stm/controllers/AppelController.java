package mg.ituproject.stm.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Appel;
import mg.ituproject.stm.models.Client;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/client")
public class AppelController {
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/appels/{idClient}")
	public WebServiceObject historiqueAppels(@PathVariable("idClient") String idClient, HttpServletRequest request){
		try {
			String token = Token.extract(request);
			Client client = new Client(idClient, new Token(idClient, token));
			List<Appel> appels = client.getHistoriqueAppels(mongoTemplate);
			return new WebServiceObject(200, "ok", appels);
		} catch (ControlException ex) {
			Map<String, String> map = new HashMap<>();
			map.put("champs", ex.getFieldName());
			return new WebServiceObject(100, ex.getMessage(), map);
		}
	}
}
