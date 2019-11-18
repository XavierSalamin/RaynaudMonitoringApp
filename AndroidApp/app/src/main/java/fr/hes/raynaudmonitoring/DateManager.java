package fr.hes.raynaudmonitoring;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple class who generate an instance of {@link DatePickerDialog}
 * and provide static methods to handle relationships between dates
 */
public class DateManager extends DialogFragment {

    /**
     * Return an instance of {@link DatePickerDialog}
     * @param savedInstanceState state of the application
     * @return an instance of of {@link DatePickerDialog}
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    /**
     * Listener for the DatePickerDialog created on the onCreateDialog method.
     * When the user set a date, this listener will call the OnDateManagerResult method
     * from the all the Fragment who use this components.
     */
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = calendar.getTime();

                    MainFragment.OnDateManagerResult(date);
                    PicturesFragment.OnDateManagerResult(date);
                    NotesFragment.OnDateManagerResult(date);
                    AddTreatmentActivity.OnDateManagerResult(date);

                }
            };
    /**
     * Return true if the date passed as parameters is the date from Yesterday
     * @param date the Date you want to test
     * @return true if Yesterday
     */
    public static boolean isYesterday(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar calendarTested = Calendar.getInstance();
        calendarTested.setTime(date);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == calendarTested.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendarTested.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendarTested.get(Calendar.DATE);
    }

    /**
     * Return true if the date passed as parameters is the date from Tomorrow
     * @param date the Date you want to test
     * @return true if Tomorrow
     */
    public static boolean isTomorrow(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar calendarTested = Calendar.getInstance();
        calendarTested.setTime(date);

        now.add(Calendar.DATE,+1);

        return now.get(Calendar.YEAR) == calendarTested.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendarTested.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendarTested.get(Calendar.DATE);
    }

    /**
     * Return true if the date passed as parameters is the date from Today
     * @param date the Date you want to test
     * @return true if Today
     */
    public static boolean isToday(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar calendarTested = Calendar.getInstance();
        calendarTested.setTime(date);

        return now.get(Calendar.YEAR) == calendarTested.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendarTested.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendarTested.get(Calendar.DATE);
    }

    /**
     * This method set the text of a TextView based on a selected date
     * the text will be a relative date (Today, Yesterday, Tomorrow)
     * or will follow this format : thursday, june 21 2018
     *
     * @param date the selected date
     * @param textView the TextView you want to set the text
     */
    public static void setTextViewFormattedDate(Date date, TextView textView){

        Calendar calendar = Calendar.getInstance();
        if(calendar != null && date!=null)  {
            calendar.setTime(date);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM yyyy");
            String formattedDate = sdf.format(calendar.getTime());
            if(DateManager.isYesterday(date)){
                textView.setText(R.string.yesterday);
            }
            else if(DateManager.isToday(date)){
                textView.setText(R.string.today);
            }
            else if(DateManager.isTomorrow(date)){
                textView.setText(R.string.tomorrow);
            }
            else{
                textView.setText(formattedDate);
            }
        }


    }


}
