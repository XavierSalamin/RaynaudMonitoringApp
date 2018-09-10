package fr.hes.raynaudmonitoring;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Dictionary;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * This activity generates a report in pdf format containing all the data stored in the database
 */
public class Settings extends Fragment {


    ArrayList<String> titleList;
    ArrayList<String> timeList;
    ArrayList<String> rcsList;
    ArrayList<String> rcsDayList;
    ArrayList<String> rcsMonthList;
    ArrayList<String> rcsYearList;
    ArrayList<String> treatmentTime;
    //Crisis members
    ArrayList<String> startPicture;
    ArrayList<String> startThermPicture;
    ArrayList<String> endPicture;
    ArrayList<String> endThermPicture;

    ArrayList<String> dateCrisis;
    ArrayList<String> timeStart;
    ArrayList<String> timeEnd;
    ArrayList<String> pain;

    ArrayList<String> newRcsList;
    ArrayList<String> dateRcs;

    Button button;

    EditText numberEdit;
    EditText phaseEdit;
    EditText nameEdit;

    String numberPatient;
    String phase;
    String namePdf;

    int counterCrisis = 0;
    String durationCrisis;
    String averageRCS;
    int counterSideEffects = 0;


    ArrayList<String> commentaryList;
    ArrayList<String> dateCommentary;

    /**
     * Return a fragment of this class to be instantiated in {@link MainActivity}
     * @return Instance of Settings
     */
    public static Settings newInstance() {
        Settings fragment = new Settings();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.activity_settings, container, false);
        button = rootView.findViewById(R.id.pdf_button);
        nameEdit = rootView.findViewById(R.id.edit_name_pdf);
        phaseEdit = rootView.findViewById(R.id.edit_phase_treatment);
        numberEdit = rootView.findViewById(R.id.edit_number_client);


        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                namePdf = s.toString();
            }
        });

        phaseEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                phase = s.toString();
            }
        });

        numberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                numberPatient = s.toString();
            }
        });






        titleList = new ArrayList<>();
        timeList = new ArrayList<>();

        startPicture = new ArrayList<>();
        startThermPicture = new ArrayList<>();
        endPicture = new ArrayList<>();
        endThermPicture = new ArrayList<>();

        newRcsList = new ArrayList<>();
        dateRcs = new ArrayList<>();

        dateCrisis = new ArrayList<>();
        timeStart = new ArrayList<>();
        timeEnd = new ArrayList<>();
        pain = new ArrayList<>();

        treatmentTime = new ArrayList<>();
        rcsList = new ArrayList<>() ;
        rcsDayList = new ArrayList<>();
        rcsMonthList = new ArrayList<>();
        rcsYearList = new ArrayList<>();


        commentaryList = new ArrayList<>();
        dateCommentary = new ArrayList<>();



        try {
            retrieveDataFromDatabase();
            retrieveTreatment();
            retrieveRCS();
            retrieveCommentary();
            retrieveCrisis();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    generatePDF();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            generateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return rootView;

    }



    private void generateJson() throws JSONException {

        JSONObject obj = new JSONObject();



        JSONArray treatment = new JSONArray();

        for(int i = 0; i < rcsList.size(); i ++){
            JSONObject tr = new JSONObject();
                     tr.put("Type", "Traitement");
                    tr.put("Date",rcsDayList.get(i)+"."+rcsMonthList.get(i)+"."+rcsYearList.get(i));
                    tr.put("Description",rcsList.get(i));
                    tr.put("Heure",treatmentTime.get(i));
            treatment.put(tr);
        }

        for(int i = 0; i < commentaryList.size(); i ++) {
            JSONObject c = new JSONObject();
            c.put("Type", "Commentaire");
            c.put("Date",dateCommentary.get(i));
            c.put("Commentaire",commentaryList.get(i));
            treatment.put(c);
        }


        for(int i = 0; i < startPicture.size(); i ++){
            JSONObject cr = new JSONObject();
            cr.put("Type", "Crise");
            cr.put("Date", dateCrisis.get(i));
            cr.put("Début", timeStart.get(i));
            cr.put("Fin", timeEnd.get(i));
            cr.put("Douleur", pain.get(i));
            treatment.put(cr);
        }





        JSONArray comment = new JSONArray();
        for(int i = 0; i < commentaryList.size(); i ++) {
            JSONObject c = new JSONObject();
            c.put("Type", "Commentaire");
            c.put("Date",dateCommentary.get(i));
            c.put("Commentaire",commentaryList.get(i));
            treatment.put(c);
        }

        for(int i = 0; i < startPicture.size(); i ++){
            JSONObject cr = new JSONObject();
            cr.put("Type", "Crise");
            cr.put("Date", dateCrisis.get(i));
            cr.put("Début", timeStart.get(i));
            cr.put("Fin", timeEnd.get(i));
            cr.put("Douleur", pain.get(i));
            treatment.put(cr);
        }
        obj.put("Crises", treatment);



        obj.put("Commentaires", comment);


        JSONArray scores = new JSONArray();
        for(int i = 0; i < newRcsList.size(); i ++) {
            JSONObject rc = new JSONObject();
            rc.put("Type", "RCS");
            rc.put("Date",dateRcs.get(i));
            rc.put("RCS",newRcsList.get(i));
            scores.put(rc);
        }
        obj.put("Scores", scores);

        JSONArray crisis = new JSONArray();
        for(int i = 0; i < startPicture.size(); i ++){
            JSONObject cr = new JSONObject();
            cr.put("Type", "Crise");
            cr.put("Date", dateCrisis.get(i));
            cr.put("Début", timeStart.get(i));
            cr.put("Fin", timeEnd.get(i));
            cr.put("Douleur", pain.get(i));
            crisis.put(cr);
            }
            obj.put("Crises", crisis);

        //Create the JSON file
        FileOutputStream fop = null;
        File file;
        try {

            file = createJsonFile();
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            // get the content in bytes
            byte[] contentInBytes = obj.toString().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * Formats the data to be used with the IText library
     * @throws Exception
     */
    public void generatePDF()throws Exception{
        Document document=new Document();
        File gpxfile = createFile();
        PdfWriter.getInstance(document,new FileOutputStream(gpxfile));
        document.open();

        //Header
        //Logo
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.logo_launch);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        byte[] byteArray = stream.toByteArray();
        try {
            img = Image.getInstance(byteArray);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        };
        //Logo

        img.scaleAbsolute(100,100);
        PdfPCell image = new PdfPCell(img, false);
        image.setBorder(Rectangle.NO_BORDER);

        PdfPTable header = new PdfPTable(2);

        header.setWidthPercentage(100);
        header.setWidths(new int[]{1, 2});
        header.addCell(image);

        Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
        Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);


        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph("Ce rapport a été rédigé automatiquement."+Chunk.NEWLINE+"" +
                "Numéro de patient : "+numberPatient+Chunk.NEWLINE+"" +
                "Phase de traitement : "+phase);

        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);

        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorder(Rectangle.NO_BORDER);

        header.addCell(cell);

        document.add(header);



        document.add(new Chunk(new LineSeparator()));


        document.add(new Paragraph("Traitements : ", chapterFont));
        // add a couple of blank lines
        document.add( Chunk.NEWLINE );

        //Table
        PdfPTable table = new PdfPTable(5); // 3 columns.
        float[] columnWidths = {0.4f, 0.4f, 0.5f, 3f, 0.5f};

        table.setWidths(columnWidths);
        table.addCell(new PdfPCell(new Paragraph( "Jour" )));
        table.addCell(new PdfPCell(new Paragraph("Mois" )));
        table.addCell(new PdfPCell(new Paragraph( "Année"  )));
        table.addCell(new PdfPCell(new Paragraph( "Description" )));
        table.addCell(new PdfPCell(new Paragraph( "Heure" )));

        for(int i = 0; i < rcsList.size(); i ++){
            table.addCell(new PdfPCell(new Paragraph(   rcsDayList.get(i))));
            table.addCell(new PdfPCell(new Paragraph(   rcsMonthList.get(i))));
            table.addCell(new PdfPCell(new Paragraph(   rcsYearList.get(i))));
            table.addCell(new PdfPCell(new Paragraph(   rcsList.get(i))));
            table.addCell(new PdfPCell(new Paragraph(   treatmentTime.get(i))));
        }


        document.add(table);

        //End Table

        document.add(new Paragraph("Commentaires : ", chapterFont));
        document.add( Chunk.NEWLINE );
        PdfPTable tableStatistics = new PdfPTable(4); // 3 columns.

/*
        tableStatistics.addCell(new PdfPCell(new Paragraph( "Fréquence moyenne/jour" )));
        tableStatistics.addCell(new PdfPCell(new Paragraph("Durée moyenne par jour" )));
        tableStatistics.addCell(new PdfPCell(new Paragraph( "RCS moyen"  )));
        tableStatistics.addCell(new PdfPCell(new Paragraph( "Nb d'effets indésirables" )));

        tableStatistics.addCell(new PdfPCell(new Paragraph( counterCrisis+"")));
        tableStatistics.addCell(new PdfPCell(new Paragraph(durationCrisis )));
        tableStatistics.addCell(new PdfPCell(new Paragraph( averageRCS )));
        tableStatistics.addCell(new PdfPCell(new Paragraph( counterSideEffects +"")));

        document.add(tableStatistics);
        */

        //Crisis




        PdfPTable tableCommentary = new PdfPTable(2); // 3 columns.


        tableCommentary.addCell(new PdfPCell(new Paragraph( "Date" )));
        tableCommentary.addCell(new PdfPCell(new Paragraph("Commentaire" )));

        for(int i = 0; i < commentaryList.size(); i ++) {
            tableCommentary.addCell(new PdfPCell(new Paragraph(dateCommentary.get(i) + "")));
            tableCommentary.addCell(new PdfPCell(new Paragraph(commentaryList.get(i))));
        }

        document.add(tableCommentary);
        document.add(new Paragraph("Crises : ", chapterFont));
        document.add( Chunk.NEWLINE );


        //Table
        int imgWidth = 320;
        int imgHeight = 180;
        for(int i = 0; i < startPicture.size(); i ++){
            PdfPTable tableCrisis = new PdfPTable(2); // 3 columns.
            String startPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+startPicture.get(i)+".jpg";
            String endPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+endPicture.get(i)+".jpg";
            String startThermPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+startThermPicture.get(i)+".jpg";
            String endThermPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)+ "/"+endThermPicture.get(i)+".jpg";
            File imgStart = new File(startPath);
            if(imgStart.exists())
            {
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Crise du : " +dateCrisis.get(i))));
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Douleur : " +pain.get(i))));
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Début : " +timeStart.get(i))));
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Fin : " +timeEnd.get(i))));
                Image imgC = Image.getInstance(startPath);
                imgC.scaleAbsolute(imgWidth,imgHeight);
                imgC.setRotationDegrees(270);
                PdfPCell imageC = new PdfPCell(imgC, false);
                tableCrisis.addCell(imageC);


            File imgEnd = new File(endPath);
            if(imgEnd.exists())
            {
                Image imcE = Image.getInstance(endPath);
                imcE.scaleAbsolute(imgWidth,imgHeight);
                imcE.setRotationDegrees(270);
                PdfPCell imageE = new PdfPCell(imcE, false);
                tableCrisis.addCell(imageE);
            }
            else{
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Pas de photo de fin" )));
            }



                File imgStartT = new File(startThermPath);
            if(imgStartT.exists())
            {
                Image imgST = Image.getInstance(startThermPath);
                imgST.scaleAbsolute(imgHeight,imgWidth);
                imgST.setRotationDegrees(0);
                PdfPCell imageST = new PdfPCell(imgST, false);
                tableCrisis.addCell(imageST);
            }
            else{
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Pas de thermo début" )));
            }

            File imgEndT = new File(endThermPath);
            if(imgEndT.exists())
            {
                Image imgET = Image.getInstance(endThermPath);
                imgET.scaleAbsolute(imgHeight,imgWidth);
                imgET.setRotationDegrees(0);
                PdfPCell imageET = new PdfPCell(imgET, false);
                tableCrisis.addCell(imageET);
            }
            else{
                tableCrisis.addCell(new PdfPCell(new Paragraph( "Pas de thermo fin" )));
            }

            }

            document.add(tableCrisis);

            document.newPage();

        }




        //End Crisis







        document.add(new Chunk(new LineSeparator()));


        for(int i = 0; i < titleList.size(); i++){
            document.add(new Paragraph(titleList.get(i)+" "+timeList.get(i)));
        }

        document.close();
        Toast.makeText(getContext(), "PDF generate", Toast.LENGTH_SHORT).show();
    }

    private File createFile() throws IOException {
        String pdfFileName = namePdf;
        File storageDir =getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File image = new File(storageDir, pdfFileName+".pdf");

        return image;


    }

    private File createJsonFile() throws IOException {
        File storageDir =getActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File image = new File(storageDir, "data.json");

        return image;


    }



    public void retrieveDataFromDatabase()throws CouchbaseLiteException {
        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("reminder")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");


                    titleList.add(all.getString("title"));
                    timeList.add(all.getInt("hour")+":"+all.getInt("minute"));
        }
    }

    public void retrieveTreatment()throws CouchbaseLiteException {
        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("treatment")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");

            treatmentTime.add(all.getInt("hour")+":"+all.getInt("minute"));
            if(all.getBoolean("sideEffects")){
                counterSideEffects++;
            }
            rcsList.add(all.getString("description")+"");
            rcsDayList.add(all.getInt("day")+"");
            rcsMonthList.add(all.getInt("month")+"");
            rcsYearList.add(all.getInt("year")+"");

        }
    }




    public void retrieveCommentary()throws CouchbaseLiteException {
        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("commentary")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");


            commentaryList.add(all.getString("commentary")+"");

            dateCommentary.add(all.getInt("day")+"."+
                    all.getInt("month")+"."+
                    all.getInt("year"));

        }
    }


    public void retrieveRCS()throws CouchbaseLiteException {
        //We get all the reminders from the database
        int counter = 0;
        int rcs = 0;
        double resultRCS;
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("rcs")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");
            counter++;
            newRcsList.add(all.getString("rcs")+"");

            dateRcs.add(all.getInt("day")+"."+
                    all.getInt("month")+"."+
                    all.getInt("year"));

            rcs += all.getInt("rcs");


        }

        resultRCS = rcs/counter;
         averageRCS = String.format("%.2f", resultRCS);
    }



    public void retrieveCrisis()throws CouchbaseLiteException {

        int counterHour=0;
        int counterMinute=0;
        //We get all the reminders from the database
        Query query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(DatabaseManager.getDatabase()))
                .where(Expression.property("type").equalTo(Expression.string("crisis")));

        ResultSet rs = query.execute();
        for (Result result : rs) {
            Dictionary all = result.getDictionary("staging");


            //Average Duration
            if(all.getString("startName")!=null&&all.getString("endName")!=null) {
                counterCrisis++;
                counterHour = (all.getInt("hourEnd") - all.getInt("hourStart"));
                counterMinute = (all.getInt("minuteEnd") - all.getInt("minuteStart"));
            }

            startPicture.add(all.getString("startName")+"");
            startThermPicture.add(all.getString("thermStartName")+"");
            endPicture.add(all.getString("endName")+"");
            endThermPicture.add(all.getString("thermEndName")+"");

            dateCrisis.add(all.getInt("day")+"."+
                    all.getInt("month")+"."+
                    all.getInt("year"));

            timeStart.add(all.getString("startText")+"");
            pain.add(all.getInt("pain")+"");
            timeEnd.add(all.getString("endText")+"");
        }
       // durationCrisis =  counterHour/counterCrisis+":"+ (counterMinute/counterCrisis);
    }
}