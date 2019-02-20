package fr.hes.raynaudmonitoring.backend.backend.dao;

import java.util.Date;

import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrisisByDay {

	private final CrisisDoc crisis;
	private final Date date;

	public CrisisByDay(final CrisisDoc crisis, final Date date) {
		this.crisis = crisis;
		this.date = date;
	}

}
