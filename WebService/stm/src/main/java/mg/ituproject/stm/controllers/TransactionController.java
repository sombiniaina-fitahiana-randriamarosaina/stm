package mg.ituproject.stm.controllers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import mg.ituproject.stm.models.Transaction;
import mg.ituproject.stm.utils.databases.ConnectionHelper;
import mg.ituproject.stm.utils.webServices.WebServiceObject;

public class TransactionController {
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping(value = "/depot-non-valider")
	public WebServiceObject getDepotNonValider(){
		Connection connection = null;
		try {
			connection = ConnectionHelper.getConnection();
			// mila maka token
			List<Transaction> lc = Transaction.findDepotsNonValider(connection);
			return new WebServiceObject(200, "ok", lc);
		}	
		catch(SQLException | ClassNotFoundException ex) {
			return new WebServiceObject(500, ex.getMessage(), null);
		}
		finally {
			ConnectionHelper.closeConnection(connection);
		}
	}
}
