package fr.hes.raynaudmonitoring;

import java.util.Calendar;
import java.util.Date;

public class Crisis {

private String startFileName;
private String endFileName;

private String thermStartName;
private String thermEndName;

private Date date;
private String startText;
private String endText;
private int pain;
private int hourStart, hourEnd, minuteStart, minuteEnd;
private boolean isNotes;

    public Crisis(String start, String end, Date date){
        this.startFileName = start;
        this.endFileName = end;
        this.date =date;
        startText="";
        endText="";
    }
    public Crisis(String start, String end, Date date, String startText, String endText, int pain, String thermStartName, String thermEndName){
        this.startFileName = start;
        this.endFileName = end;

        this.thermStartName = thermStartName;
        this.thermEndName = thermEndName;
        this.date =date;
        this.startText=startText;
        this.endText=endText;
        this.pain=pain;
    }
    public Crisis(String start, String end, Date date, String startText, String endText, int pain, String thermStartName, String thermEndName, int hourStart,  int minuteStart, int hourEnd,int minuteEnd){
        this.startFileName = start;
        this.endFileName = end;
        this.date =date;
        this.startText=startText;
        this.endText=endText;
        this.pain=pain;
        this.thermStartName = thermStartName;
        this.thermEndName = thermEndName;
        this.hourStart=hourStart;
        this.minuteStart=minuteStart;
        this.hourEnd=hourEnd;
        this.minuteEnd=minuteEnd;
    }

    public Crisis(Date date, String startText, String endText, int pain, int hourStart,  int minuteStart, int hourEnd,int minuteEnd){
        this.startFileName =  null;
        this.endFileName = null;
        this.date =date;
        this.startText=startText;
        this.endText=endText;
        this.pain=pain;
        this.hourStart=hourStart;
        this.minuteStart=minuteStart;
        this.hourEnd=hourEnd;
        this.minuteEnd=minuteEnd;
    }

    public Crisis(String startName, String endName, Date date, String startText, String endText, int pain, String thermStartName, String thermEndName, int hourStart, int minuteStart, int hourEnd, boolean isNotes, int minuteEnd) {
        this.startFileName = startName;
        this.endFileName = endName;
        this.date =date;
        this.startText=startText;
        this.endText=endText;
        this.pain=pain;
        this.thermStartName = thermStartName;
        this.thermEndName = thermEndName;
        this.hourStart=hourStart;
        this.minuteStart=minuteStart;
        this.hourEnd=hourEnd;
        this.isNotes=isNotes;
        this.minuteEnd=minuteEnd;


    }

    public String getStartFileName() {
        return startFileName;
    }

    public void setStartFileName(String startFileName) {
        this.startFileName = startFileName;
    }

    public String getEndFileName() {
        return endFileName;
    }

    public void setEndFileName(String endFileName) {
        this.endFileName = endFileName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String r ="";
        if(startFileName!=null)
            r="Début ";
        if(endFileName!=null)
            r="Fin";
        return r;
    }

    public String getStartText() {
        return startText;
    }


    public String getEndText() {
        return endText;
    }


    public int getPain() {
        return pain;
    }

    public int getHourStart() {
        return hourStart;
    }


    public int getHourEnd() {
        return hourEnd;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public int getMinuteEnd() {
        return minuteEnd;
    }

    public String getThermStartName() {
        return thermStartName;
    }

    public String getThermEndName() {
        return thermEndName;
    }

    public boolean isNotes() {
        return isNotes;
    }

    public void setNotes(boolean notes) {
        isNotes = notes;
    }
}
