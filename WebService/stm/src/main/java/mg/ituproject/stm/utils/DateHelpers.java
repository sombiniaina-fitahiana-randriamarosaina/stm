
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mg.ituproject.stm.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sombiniaina
 */
public class DateHelpers {
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    public static final int DAY = 0;
    public static final int HOUR = 1;
    public static final int MINUTE = 2;
    public static final int SECOND = 3;
    
    public static double getDifference(Date start, Date finish, int returnType) throws IllegalArgumentException{
        Date date = new Date(finish.getTime() - start.getTime());
        switch (returnType) {
            case DateHelpers.DAY:
                return toDay(date);
            case DateHelpers.HOUR:
                return toHour(date);
            case DateHelpers.MINUTE:
                return toMinutes(date);
            case DateHelpers.SECOND:
                return toSecond(date);
            default:
                throw new IllegalArgumentException("Return type Invalise");
        }
    }
    
    public static Integer[] getOccurrenceDay(Date start, Date end, boolean includeEndate){
        
        try {
            SimpleDateFormat dayFormat = new SimpleDateFormat("u");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            int dayOfStart = Integer.parseInt(dayFormat.format(start));
            int dayOfEnd = Integer.parseInt(dayFormat.format(end));
            
            Date mondayStart = Convert.toDate(dateFormat.format(addDays(start, -(dayOfStart - 1))));
            Date mondayEnd = Convert.toDate(dateFormat.format(addDays(end, -(dayOfEnd - 1) + 7)));
            
            double numberWeek = DateHelpers.getDifference(mondayStart, mondayEnd, DAY )/ 7;
            
            Integer[] occ = new Integer[7];
            
            for (int i = 0; i < occ.length; i++)
                occ[i] = (int)numberWeek;
            
            for (int i = 1; i < dayOfStart; i++)
                if(occ[i - 1] > 0) occ[i - 1]--;
            
            for (int i = dayOfEnd; i < 8; i++)
                if(occ[i - 1] > 0) occ[i - 1]--;
            
            if(includeEndate)
                occ[dayOfEnd-1]++;
            
            return occ;
        } catch (ParseException ex) {
            Logger.getLogger(DateHelpers.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static double toSecond(Date date){
        return date.getTime() / 1000;
    }
    
    public static double toMinutes(Date date){
        return toSecond(date) / 60;
    }
    
    public static double toHour(Date date){
        return toMinutes(date) / 60;
    }
    
    public static double toDay(Date date){
        return toHour(date) / 24;
    }
    
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }
    
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }
    
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
	cal.setTime(date);
	cal.add(Calendar.DATE, days);
			
	return cal.getTime();
    }
    
    public static Time toTime(Date date){
        return Convert.toTime(timeFormat.format(date));
    }
    public static boolean isTheSameDate(Date d1, Date d2){
        return (Convert.DATE_FORMAT.format(d1).compareToIgnoreCase(Convert.DATE_FORMAT.format(d2)) == 0 ) ? true : false;
    }
    
    public static Timestamp getFinJournee() {
 	   try {
 		  String s = new SimpleDateFormat("dd-MM-yyyy").format(addDays(new Date(), 1)) + " 00:00:00";
		return Convert.toTimestamp(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
 	   return null;
    }
}
