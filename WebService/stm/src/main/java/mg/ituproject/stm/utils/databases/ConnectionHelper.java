/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.ituproject.stm.utils.databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author sombiniaina
 */
public class ConnectionHelper {
	public static MongoHelper connectMongoDB() {
		return new MongoHelper("localhost", 27017, "stm");
	}
	
    public static Connection getConnection() throws ClassNotFoundException, SQLException{
    	String user = "admin_stm";
		String password = "123456";
		String database = "stm";
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost/%s?user=%s&password=%s";
        return DriverManager.getConnection(String.format(url, database, user, password));
    }
    public static void closeConnection(Connection connection){
        try {
            if (connection != null) connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}