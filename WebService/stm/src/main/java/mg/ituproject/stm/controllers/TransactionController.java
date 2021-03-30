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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mg.ituproject.stm.models.Admin;
import mg.ituproject.stm.models.Token;
import mg.ituproject.stm.models.Transaction;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.exceptions.ControlException;
import mg.ituproject.stm.utils.exceptions.ValidateException;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

@RestController
@RequestMapping("/admin")
public class TransactionController {
	@Autowired
	MongoTemplate mongoTemplate;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/depot-non-valider")
	public WebServiceObject getDepotNonValider(HttpServletRequest request){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			String token = Token.extract(request);
			List<Transaction> lc = Transaction.findDepotsNonValider(connection, mongoTemplate, token);
			return new WebServiceObject(200, "ok", lc);
		}	
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		} catch (ControlException e) {
			return new WebServiceObject(500, e.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}
	
	@PostMapping("/valider-depot")
	@ResponseBody
	public WebServiceObject validerDepot(HttpServletRequest request, @RequestBody Transaction transaction) throws Exception{
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			String token = Token.extract(request);
			Admin.validerDepot(connection, mongoTemplate, transaction, token);
			return new WebServiceObject(200, "validation effectuee!!", null);
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
}
