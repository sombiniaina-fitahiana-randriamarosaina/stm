/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.ituproject.stm.utils;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 *
 * @author sombiniaina
 */
public class Convert {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    
    public static boolean toBoolean(String value) throws ParseException{
        if (value.compareToIgnoreCase("true") * value.compareToIgnoreCase("false") != 0)
            throw new ParseException(String.format("Impossible de convertir [%s] en boolean", value), 0);     
        return Boolean.parseBoolean(value);
    }
    
    public static int toInt(String value) throws ParseException{
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new ParseException(String.format("Impossible de convertir [%s] en nombre", value), 0);
        }
    }
    
    public static BigDecimal toBigDecimal(String value)throws ParseException{
        try {
            return BigDecimal.valueOf(Double.valueOf(value));
        } catch (Exception e) {
            throw new ParseException(String.format("Impossible de convertir [%s] en nombre", value), 0);
        }
    }
    
    public static Date toDate(String value)throws ParseException{
        try{
            return new Date(Convert.DATE_FORMAT.parse(value).getTime()); 
        }
        catch(ParseException e){
            throw new ParseException(String.format("Impossible de convertir [%s] en date[yyyy-mm-dd]", value), 0);
        } 
    }
    
    public static Time toTime(String value)throws IllegalArgumentException{
        try{
            return new Time(Convert.TIME_FORMAT.parse(value).getTime()); 
        }
        catch(ParseException e){
            throw new IllegalArgumentException("format Time valide : HH:mm:ss");
        } 
    }
    
    public static Timestamp toTimestamp(String value)throws ParseException{
        try{
            return new Timestamp(Convert.TIMESTAMP_FORMAT.parse(value).getTime()); 
        }
        catch(ParseException e){
            throw new ParseException( String.format("Impossible de convertir [%s] en date[yyyy-MM-dd HH:mm:ss]", value), 0);
        } 
    }
}
