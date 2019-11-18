package fr.hes.raynaudmonitoring.utils;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.hes.raynaudmonitoring.dao.CrisisByDay;
import fr.hes.raynaudmonitoring.dao.doc.CrisisDoc;
import fr.hes.raynaudmonitoring.dao.doc.TreatmentDoc;

public class CrisisUtils {

	public static int getNumberOfCrisisPerDay(final List<CrisisDoc> crisisList, final Date date) {

		return crisisList.stream().filter(crisis -> getDate(crisis).compareTo(date) == 0).collect(Collectors.toList())
				.size();

	}

	public static Date getDate(final CrisisDoc crisis) {

		return new Date(crisis.getYear(), crisis.getMonth(), crisis.getDay());
	}

	public static Date getDateForTreatment(final TreatmentDoc treatment) {

		return new Date(treatment.getYear(), treatment.getMonth(), treatment.getDay());
	}

	public static int getTotalDurationByDate(final List<CrisisDoc> crisisList, final Date date) {

		int durationInMinutes = 0;

		for (int i = 0; i < crisisList.size(); i++) {

			if (getDate(crisisList.get(i)).compareTo(date) == 0) {
				if (crisisList.get(i).getEndText() != null && crisisList.get(i).getStartText() != null)
					durationInMinutes += (crisisList.get(i).getHourEnd() - crisisList.get(i).getHourStart()) * 60;
				durationInMinutes += crisisList.get(i).getMinuteEnd() - crisisList.get(i).getMinuteStart();
			}
		}

		return durationInMinutes;
	}

	public static double getAveragePainByDate(final List<CrisisDoc> crisisList, final Date date) {

		int painCounter = 0;

		double average = 0;

		for (int i = 0; i < crisisList.size(); i++) {

			if (getDate(crisisList.get(i)).compareTo(date) == 0) {
				painCounter += crisisList.get(i).getPain();
			}
		}

		if (getNumberOfCrisisPerDay(crisisList, date) == 0) {
			average = 0;
		} else {
			average = painCounter / getNumberOfCrisisPerDay(crisisList, date);
			final DecimalFormat df = new DecimalFormat("#.##");
			average = Double.valueOf(df.format(average));

		}

		return average;
	}

	public static List<CrisisByDay> getListOfCrisisOfCrisisByDay(final List<CrisisDoc> crisisList) {

		final Map<Date, CrisisDoc> mapCrisis = new HashMap<>();
		// Les mettre dans une map ça va enlever les duplications. Comme ils auront la
		// même clef

		for (int i = 0; i < crisisList.size(); i++) {
			mapCrisis.put(getDate(crisisList.get(i)), crisisList.get(i));
		}

		final List<CrisisByDay> result = new ArrayList<>();

		mapCrisis.forEach((date, value) -> {
			result.add(new CrisisByDay(value, date));
		});

		return result;

	}

	public static int getNumberOfTreatmentByDay(final List<TreatmentDoc> treatmentList, final Date date) {

		return treatmentList.stream().filter(treatment -> getDateForTreatment(treatment).compareTo(date) == 0)
				.collect(Collectors.toList()).size();

	}
}