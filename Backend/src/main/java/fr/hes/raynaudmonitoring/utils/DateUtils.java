package fr.hes.raynaudmonitoring.utils;



import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.hes.raynaudmonitoring.dao.doc.CrisisDoc;

public class DateUtils {

	public static int getDayOfWeekForCrisis(final CrisisDoc crisis) {

		final Calendar c = Calendar.getInstance();

		final Date date = new Date(crisis.getDay(), crisis.getMonth(), crisis.getYear());

		c.setTime(date);

		return c.get(Calendar.DAY_OF_WEEK);

	}

	public static String getFormattedDateForCrisis(final CrisisDoc crisis) {

		final Calendar c = Calendar.getInstance();

		final Date date = new Date(crisis.getYear() - 1900, crisis.getMonth(), crisis.getDay());

		c.setTime(date);

		// *** same for the format String below
		final SimpleDateFormat dt1 = new SimpleDateFormat("yyyy.MM.dd");
		return dt1.format(date);
	}

}
