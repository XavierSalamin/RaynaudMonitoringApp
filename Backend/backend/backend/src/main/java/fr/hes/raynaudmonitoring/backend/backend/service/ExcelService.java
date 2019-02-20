package fr.hes.raynaudmonitoring.backend.backend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hes.raynaudmonitoring.backend.backend.dao.CrisisByDay;
import fr.hes.raynaudmonitoring.backend.backend.dao.RcsRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.TreatmentRepository;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.CrisisDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.RcsDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.TreatmentDoc;
import fr.hes.raynaudmonitoring.backend.backend.dao.doc.UserProfileDoc;
import fr.hes.raynaudmonitoring.backend.backend.utils.CrisisUtils;
import fr.hes.raynaudmonitoring.backend.backend.utils.DateUtils;

@Service
public class ExcelService {

	protected static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

	private static String FILENAME = "raynaud_data";
	private static String FILEDIR = "/export";
	@Autowired
	UserRequestServiceImpl userRequestService;

	@Autowired
	CrisisServiceImpl crisisService;

	@Autowired
	RcsRepository rcsRepository;

	@Autowired
	TreatmentRepository treatmentRepository;

	public void generateExcelById(final Integer patientNumber, final HttpServletResponse response) {
		final Workbook wb = new HSSFWorkbook();
		final CreationHelper createHelper = wb.getCreationHelper();

		if (patientNumber != null) {
			// TODO get CrisisDoc;

			final String userId = userRequestService.getProfileIdByPatientNumber(patientNumber);
			if (userId == null) {
				logger.info("EXCEL WAS NOT GENERATED");
				return;
			}

			final List<CrisisDoc> crisisList = crisisService.findCrisisByUser(userId);
			if (crisisList == null) {
				logger.error("Didn't found crisis for the {}", userId);
				return;
			}

			logger.info("{} crisis will be exported", crisisList.size());

			final Sheet sheet = wb.createSheet("test");

			// Headers Creations
			final Row headersRow = sheet.createRow(0);

			// GetTreatmentList

			final List<TreatmentDoc> treatmentList = treatmentRepository.findAll();

			// Create and set the headers cell
			final List<String> headers = getHeaders();
			for (int i = 0; i < headers.size(); i++) {
				final Cell cell = headersRow.createCell(i);
				cell.setCellValue(createHelper.createRichTextString(headers.get(i)));
			}

			final UserProfileDoc user = userRequestService.getProfileByPatientNumber(patientNumber);

			/**
			 * Transform CrisisList to CrisisByDayList (Since each row represent one day
			 */

			final List<CrisisByDay> crisisByDayList = CrisisUtils.getListOfCrisisOfCrisisByDay(crisisList);

			// Row with the crisis data
			final String initiales = getInitiales(user);
			final List<Row> crisisRowList = new ArrayList<>();
			for (int i = 0; i < crisisByDayList.size(); i++) {
				crisisRowList.add(sheet.createRow(i + 1)); // +1 because first row is for the headers
			}

			// Créer une liste de crisisPerDay

			// set Crisis data for each row

			for (int i = 0; i < crisisRowList.size(); i++) {
				final CrisisByDay crisis = crisisByDayList.get(i);
				// Initiales
				crisisRowList.get(i).createCell(0).setCellValue(createHelper.createRichTextString(getInitiales(user)));
				// Numéro du patient
				crisisRowList.get(i).createCell(1).setCellValue(createHelper.createRichTextString(patientNumber + ""));
				// Numéro de randomisation
				crisisRowList.get(i).createCell(2)
						.setCellValue(createHelper.createRichTextString(user.getRandomisationNumber() + ""));
				// Cycle
				crisisRowList.get(i).createCell(3)
						.setCellValue(createHelper.createRichTextString(user.getCycleNumber() + ""));
				// Numéro de Semaine

				crisisRowList.get(i).createCell(4).setCellValue(createHelper.createRichTextString(""));
				// Numéro de Jour Semaine
				crisisRowList.get(i).createCell(5).setCellValue(
						createHelper.createRichTextString(DateUtils.getDayOfWeekForCrisis(crisis.getCrisis()) + ""));
				// Numéro de Jour étudde
				crisisRowList.get(i).createCell(6).setCellValue(createHelper.createRichTextString(""));
				// Date
				crisisRowList.get(i).createCell(7).setCellValue(
						createHelper.createRichTextString(DateUtils.getFormattedDateForCrisis(crisis.getCrisis())));
				// Nombre Crise
				crisisRowList.get(i).createCell(8).setCellValue(createHelper.createRichTextString(
						String.valueOf(CrisisUtils.getNumberOfCrisisPerDay(crisisList, crisis.getDate()))));
				// Douleur Moyenne
				crisisRowList.get(i).createCell(9).setCellValue(createHelper.createRichTextString(
						String.valueOf(CrisisUtils.getAveragePainByDate(crisisList, crisis.getDate()))));

				// Durée totale
				crisisRowList.get(i).createCell(10).setCellValue(createHelper.createRichTextString(
						String.valueOf(CrisisUtils.getTotalDurationByDate(crisisList, crisis.getDate()))));

				// RCS
				crisisRowList.get(i).createCell(11).setCellValue(
						createHelper.createRichTextString(String.valueOf(getRcsByDate(crisis.getDate()))));

				// Nombre prise
				crisisRowList.get(i).createCell(12).setCellValue(createHelper.createRichTextString(
						String.valueOf(CrisisUtils.getNumberOfTreatmentByDay(treatmentList, crisis.getDate()))));

				// Kit
				crisisRowList.get(i).createCell(13)
						.setCellValue(createHelper.createRichTextString(String.valueOf(user.getKitUsed())));
			}
			// TODO Add timestamps
			try {
				final File dir = new File(FILEDIR);
				if (!dir.exists()) {
					dir.mkdir(); // Create directory if not exists
				}

				final File dataFile = new File(dir, String.format("%s_%s.xls", FILENAME, patientNumber));

				final OutputStream fileOut = new FileOutputStream(dataFile);
				wb.write(fileOut);
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-disposition", "attachment; filename=" + FILENAME);
				logger.info("EXCEL GENERATION WAS A SUCCESS");

			} catch (final IOException e) {
				logger.error("There was an error creating the excel file");
			}
		}

		else {
			logger.error("EXCEL NOT GENERATED");
			logger.error("Impossible to get a UserProfile for this id");
		}

	}

	public int getRcsByDate(final Date date) {

		final List<RcsDoc> result = rcsRepository.findAll().stream()
				.filter(rcs -> new Date(rcs.getYear(), rcs.getMonth(), rcs.getDay()).compareTo(date) == 0)
				.collect(Collectors.toList());
		if (result.isEmpty())
			return 0;
		return result.get(0).getRcs();
	}

	private static List<String> getHeaders() {

		final List<String> headers = new ArrayList<>();

		headers.add("Initiales");
		headers.add("N° patient");
		headers.add("N° randomisation");
		headers.add("Cycle");
		headers.add("Semaine");
		headers.add("Jour semaine");
		headers.add("Jour étude");
		headers.add("Date");
		headers.add("Nombre Crise");
		headers.add("Douleur Moyenne");
		headers.add("Durée Totale (minutes)");
		headers.add("RCS_reel");
		headers.add("Nombre prise");
		headers.add("Kit");

		return headers;

	}

	private String getInitiales(final UserProfileDoc user) {

		final String result = "" + user.getLastname().charAt(0) + user.getLastname().charAt(1)
				+ user.getFirstname().charAt(0);
		return result.toUpperCase();

	}

}
