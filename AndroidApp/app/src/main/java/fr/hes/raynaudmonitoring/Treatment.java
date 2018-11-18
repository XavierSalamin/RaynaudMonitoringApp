package fr.hes.raynaudmonitoring;

import java.util.Date;

/**
 * Treatment Object
 */
public class Treatment {
    private int hour;
    private int minute;
    private String description;
    private boolean sideEffects;
    private Date date;

    /**
     * Constructor of the Treatment Object
     * @param hour hour of the treatment specified by the user
     * @param minute minute of the treatment specified by the user
     * @param description description written by the user
     * @param sideEffects true if the radioButton with the parameters yes from the {@link AddTreatmentActivity} has been checked
     * @param date  the date selected
     */
    public Treatment(int hour, int minute, String description, boolean sideEffects, Date date){
        this.hour = hour;
        this.minute = minute;
        this.description = description;
        this.sideEffects = sideEffects;
        this.date = date;
    }


    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public String toString() {
        String s; //Returned String
        String h;
        String m;

        //Add 0 digit behind the hour
        if(hour<10)
            h="0";
        else
            h="";

        //Same for the minutes
        if(minute<10)
            m="0";
        else
            m="";
        s = h+hour+":"+m+minute;
        return s;
    }

    public boolean isSideEffects() {
        return sideEffects;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
