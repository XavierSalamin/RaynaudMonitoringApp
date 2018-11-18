package fr.hes.raynaudmonitoring;

/**
 *  Reminder's POJO (Plain Old Java Object)
 */
public class Reminder {
    private String title;
    private int hour, minute;
    private boolean isChecked;

    /**
     * Constructor of the Reminder Object
     * @param hour hour of the reminder specified by the user
     * @param minute minute of the reminder specified by the user
     * @param title title selected by the user
     */
    Reminder(String title, int hour, int minute){
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.isChecked = false;
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
    public String getTitle() {
        return title;
    }
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
