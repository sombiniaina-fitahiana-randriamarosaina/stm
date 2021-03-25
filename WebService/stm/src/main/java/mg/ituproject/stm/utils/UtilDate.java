package mg.ituproject.stm.utils;

import java.util.Calendar;
import java.util.Date;


public final class UtilDate {

    public static final long MILISECOND_PER_DAY = 24 * 60 * 60 * 1000;

    public static final long MILLISECOND_WORKING_DAYS =  MILISECOND_PER_DAY * 5;

    public static long intervalleEnMillisSansWeekend(Date dateBegin, Date dateEnd) {
   
        Date dateBeginAtSaturday0 = findSaturdayAtSameWeekAtZero(dateBegin);
        Date calEndAtSaturday0 = findSaturdayAtSameWeekAtZero(dateEnd);

        long intervalleEnMillis = intervalleEnMillis(dateBeginAtSaturday0, calEndAtSaturday0);
        long nbSemaines = Double.valueOf(Math.floor(Long.valueOf(intervalleEnMillis).doubleValue() / 604800000)).longValue();

        long workingDaysInMillis =  nbSemaines * MILLISECOND_WORKING_DAYS;

        boolean isBeginBeforeSaturday = dateBeginAtSaturday0.getTime() - dateBegin.getTime() > 0;
        boolean isEndBeforeSaturday = calEndAtSaturday0.getTime() - dateEnd.getTime() > 0;

        long correctionInMillis = 0;

        if(isBeginBeforeSaturday) {
            correctionInMillis-= (dateBeginAtSaturday0.getTime() - dateBegin.getTime());
        }

        if(isEndBeforeSaturday) {
            correctionInMillis+= (calEndAtSaturday0.getTime() - dateEnd.getTime());
        }
        return workingDaysInMillis - correctionInMillis;

    }

    public static double intervalleEnJoursSansWeekend(Date dateBegin, Date dateEnd) {
     
        long intervalleEnMillis = intervalleEnMillisSansWeekend(dateBegin, dateEnd);
        double nbJours = (double)((intervalleEnMillis)/(double) 86400000);
        return nbJours;
    }

    public static Date findSaturdayAtSameWeekAtZero(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY :
                cal.add(Calendar.DAY_OF_MONTH, 5);
                break;
            case Calendar.TUESDAY :
                cal.add(Calendar.DAY_OF_MONTH, 4);
                break;
            case Calendar.WEDNESDAY :
                cal.add(Calendar.DAY_OF_MONTH, 3);
                break;
            case Calendar.THURSDAY :
                cal.add(Calendar.DAY_OF_MONTH, 2);
                break;
            case Calendar.FRIDAY :
                cal.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.SUNDAY :
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
        }
        return cal.getTime();
    }

    public static long intervalleEnMillis(Date dateBegin, Date dateEnd) {
        return (dateEnd.getTime() - dateBegin.getTime());
    }
    public static double intervalleEnJoursAvecWeekend(Date dateBegin,Date dateEnd)
    {
        return intervalleEnMillis(dateBegin,dateEnd)/MILISECOND_PER_DAY;
    }
   public static double heureActif(double nombreJours)
   {
        double temp=nombreJours*24;
        double toreturn=Math.floor(nombreJours)*24;
        temp=temp-toreturn;
        if(temp<=6)
        {
            if(temp<=4)
            {
                toreturn=temp; 
                return toreturn;   
            }
            toreturn=4;     
            return toreturn;
        }
        else if(temp>6)
        {
            if(temp<10)
            {
                toreturn=temp;    
                return toreturn;
            }
            toreturn=8;    
            return toreturn;
        } 
        return 0;
   }
}