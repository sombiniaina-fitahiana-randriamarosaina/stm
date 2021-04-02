/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.ituproject.stm.utils.databases;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.postgresql.jdbc.PgArray;


public class DatabaseHelper {
    
    private static <T> void set(T object, PreparedStatement pstmt, Method getter, Field field, int num) throws SQLException{
        try {
            Object value = getter.invoke(object);
            if(field.getType().equals(String.class))
                pstmt.setString(num, (String)value);
            else if(field.getType().equals(int.class))
                pstmt.setInt(num, (int)value);
            else if(field.getType().equals(double.class))
                pstmt.setDouble(num, (double)value);
            else if(field.getType().equals(BigDecimal.class))
                pstmt.setBigDecimal(num, (BigDecimal)value);
            else if(field.getType().equals(Date.class))
                pstmt.setDate(num, (Date)value);
            else
                pstmt.setObject(num, value);
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
            
        }
    }
    
    private static <T> void setObject(T object, Map<String, Field> mapFields, ResultSetMetaData metaData, ResultSet resultSet) throws SQLException{
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            try {
                String columnName = metaData.getColumnName(i);
                Field field = mapFields.get(columnName);
                if (field == null || resultSet.getObject(i) == null) {
                    continue;
                }
                Object databaseValue = resultSet.getObject(i);
                if(databaseValue.getClass().equals(Class.forName("org.postgresql.jdbc.PgArray")))
                	databaseValue = Arrays.asList(((PgArray)databaseValue).getArray());
                Method setters = object.getClass().getMethod("set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1), field.getType());
                setters.invoke(object, databaseValue);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException ex) {
            	
            } 
        }
    }
   
    private static <T> Map<String, Field> getMapFields(Class<T> type){
        Class typeT = type;
        HashMap<String, Field> mapFields = new HashMap<>();
        List<Field> fields = new ArrayList<>();
        while(!typeT.equals(Object.class)){
            fields.addAll(Arrays.asList(typeT.getDeclaredFields()));
            typeT = typeT.getSuperclass();
        }
        for (Field field : fields) {
            mapFields.put(field.getName().toLowerCase(), field);
        }
        return mapFields;
    }
    
    private static List<Method> getGetters(List<Field> listField) throws NoSuchMethodException{
        List<Method> listGetters = new ArrayList<>();
        for (Field field : listField) {
            listGetters.add( field.getDeclaringClass().getMethod("get" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1)) );
        }
        return listGetters;
    }
    
    private static String toInsertColumn(List<Field> listField){
        String string = "(";
        for (Field field : listField) 
            string += ", " + field.getName().toUpperCase();
        return string.replace("(, ", "(") + ")";
    }
    
    private static String toInsertValue(List<Field> listField){
        String string = "(";
        for (Field field : listField) 
            string += ", " + "?";
        return string.replace("(, ", "(") + ")";
    }
    
    
    public static <T> List<Field> getFields(Connection connection, Database database, Class<T> type) throws SQLException{
        List<Field> listField = new ArrayList<>();
        
        List<String> db_listColumnName = DatabaseHelper.get(connection, database.columnNameRequest(type.getSimpleName().toUpperCase()), "COLUMN_NAME", String.class);
                      
        List<Field> class_listField = new ArrayList<>();
        
        Class typeT = type;
        while(!typeT.equals(Object.class)){
            class_listField.addAll(Arrays.asList(typeT.getDeclaredFields()));
            typeT = typeT.getSuperclass();
        }
        
        for (Field field : class_listField) {
            if(db_listColumnName.contains(field.getName().toLowerCase()))
                listField.add(field);
        }
                
        return listField;
    }
         
    public static <T> List<T> find(Connection connection, String request, Class<T> type) throws SQLException, InstantiationException, IllegalAccessException{
        List<T> list = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData metaData = null;
        Map<String, Field> mapFields = null;
        try{
            stmt = connection.prepareStatement( request );
            rs = stmt.executeQuery();
            list = new ArrayList<>();
            mapFields = DatabaseHelper.getMapFields(type);
            metaData = rs.getMetaData();
            
            while(rs.next()){
                T object = type.newInstance();
                setObject(object, mapFields, metaData, rs);
                list.add(object);
            }
        }            
        finally{
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return list;
    }
    
    public static <T> T findById(Connection connection, Class<T> type, String idFieldName, Object id) throws Exception{
        String table = type.getSimpleName().toUpperCase();
        String request = String.format("SELECT * FROM %s WHERE %s=%s", table, idFieldName, (id.getClass().equals(String.class)) ? "'" + id.toString() + "'": id);
        List<T> listObject = DatabaseHelper.find(connection, request, type);
        return listObject.get(0);
    }
    
    public static <T> void insert(Connection connection, List<T> list, Database database) throws SQLException{
        List<Field> listField = null;
        List<Method> listGetter = null;
        PreparedStatement stmt = null;
        try {
            connection.setAutoCommit(false);
            listField = DatabaseHelper.getFields(connection, database, list.get(0).getClass());
            
            // remove id field
            for (int i = 0; i < listField.size(); i++) {
                if(listField.get(i).getName().toLowerCase().compareTo("id" + list.get(0).getClass().getSimpleName().toLowerCase()) == 0)
                    listField.remove(listField.get(i));
            }
            
            listGetter = DatabaseHelper.getGetters(listField);
            String columns = DatabaseHelper.toInsertColumn(listField);
            String columns_values = DatabaseHelper.toInsertValue(listField);

            connection.setAutoCommit(false);
            stmt = connection.prepareStatement( String.format("INSERT INTO %s %s VALUES %s", list.get(0).getClass().getSimpleName().toUpperCase(), columns, columns_values));
            for (T t : list) {
                for(int i = 0; i < listField.size(); i++)
                    DatabaseHelper.set(t, stmt, listGetter.get(i), listField.get(i), i+1);
                stmt.addBatch();
            }
            int[] updateCounts = stmt.executeBatch();
            for (int i=0; i<updateCounts.length; i++) {
                if (updateCounts[i] == stmt.EXECUTE_FAILED) {
                   connection.rollback();
                }
            }
            connection.commit();
        }
        catch (SQLException e) {
            try {
                connection.rollback();
            } catch (Exception ex) {
            }
            throw e;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException ex) {
            }
        }
    }
    
    public static <T> void update(Connection connection, List<T> list) throws Exception{
       
    }
    
    public static <T> List<T> get(Connection connection, String request, String column, Class<T> type) throws SQLException{
        List<T> list = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = connection.prepareStatement( request );
            rs = stmt.executeQuery();
            list = new ArrayList<>();
            while(rs.next())
                list.add((T) rs.getObject(column.toUpperCase()));
        }
        catch(SQLException e){
            throw e;
        } 
        finally{
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return list;
    }
    
    public static List<Map<String, Object>> get(Connection connection, String request, String[] columns) throws SQLException{
        List<Map<String, Object>> result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = connection.prepareStatement( request );
            rs = stmt.executeQuery();
            result = new ArrayList<>();
            while(rs.next()){
                Map<String, Object> line = new HashMap<>(columns.length);
                for (String column : columns) {
                    line.put( column, rs.getObject(column.toUpperCase()) );
                }
                result.add(line);
            }
        }
        catch(SQLException e){
            throw e;
        } 
        finally{
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }
        return result;
    }
    
    public static void update(Connection connection,String requete) throws SQLException{
    	try(PreparedStatement stmt = connection.prepareStatement(requete)){
    		stmt.execute();
    	}
    }
}
