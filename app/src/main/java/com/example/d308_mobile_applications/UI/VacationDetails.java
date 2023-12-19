package com.example.d308_mobile_applications.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_mobile_applications.R;
import com.example.d308_mobile_applications.database.Repository;
import com.example.d308_mobile_applications.entities.Excursion;
import com.example.d308_mobile_applications.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    String title;
    String hotel;
    long startDate;
    long endDate;
    int vacationID;
    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    String formattedStartDate;
    String formattedEndDate;

    ExcursionAdapter excursionAdapter;

    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startdatetext);
        editEndDate = findViewById(R.id.enddatetext);

        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        startDate = getIntent().getLongExtra("startDate", -1);
        endDate = getIntent().getLongExtra("endDate", -1);

        //Date validation
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        formattedStartDate = (startDate != -1) ? sdf.format(new Date(startDate)) : "Start Date not set";
        formattedEndDate = (endDate != -1) ? sdf.format(new Date(endDate)) : "End Date not set";

        editTitle.setText(title);
        editHotel.setText(hotel);
        editStartDate.setText(formattedStartDate);
        editEndDate.setText(formattedEndDate);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacaID", vacationID);
                startActivity(intent);
            }
        });

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editStartDate);
            }
        });

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editEndDate);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e: repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        MenuItem searchItem = menu.findItem(R.id.excursionsearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setSubmitButtonEnabled(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void performSearch(String query) {
        List<Excursion> searchResults = repository.searchExcursions(query);

        excursionAdapter.setExcursions(searchResults);
        excursionAdapter.notifyDataSetChanged();

    }

    private void showDatePickerDialog(final TextView dateField) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        //date validation
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                        dateField.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId()== R.id.vacationsave){
            Vacation vacation;

            if(editTitle.getText().toString().trim().isEmpty()){
                Toast.makeText(VacationDetails.this, "Vacation title cannot be blank", Toast.LENGTH_LONG).show();
                return true;
            }

            if(editHotel.getText().toString().trim().isEmpty()){
                Toast.makeText(VacationDetails.this, "Hotel cannot be blank", Toast.LENGTH_LONG).show();
                return true;
            }

            try {
                Date startDateParsed = sdf.parse(editStartDate.getText().toString());
                Date endDateParsed = sdf.parse(editEndDate.getText().toString());

                if (endDateParsed.before(startDateParsed)) {
                    Toast.makeText(VacationDetails.this, "End Date cannot be before Starting Date.", Toast.LENGTH_LONG).show();
                    return true;
                }


                if (vacationID == -1) {
                    if (repository.getAllVacations().size() == 0) vacationID = 1;
                    else vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;

                    vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateParsed, endDateParsed);
                    repository.insert(vacation);

                    return true;
                } else {
                    vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), startDateParsed, endDateParsed);
                    repository.update(vacation);

                    return true;
                }
            } catch (ParseException e) {
                Toast.makeText(VacationDetails.this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;

            }
        }

        if(item.getItemId()== R.id.vacationdelete){
            for(Vacation vaca:repository.getAllVacations()){
                if(vaca.getVacationID() == vacationID) currentVacation = vaca;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()){
                if (excursion.getVacationID() == vacationID) ++numExcursions;
            }
            if (numExcursions == 0){
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
                return true;
            } else {
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        if (item.getItemId() == R.id.share){
           Intent sentIntent = new Intent();
           sentIntent.setAction(Intent.ACTION_SEND);

           String message = createVacationDetails();

           sentIntent.putExtra(Intent.EXTRA_TEXT, message);
           sentIntent.setType("text/plain");
           Intent shareIntent = Intent.createChooser(sentIntent, null);
           startActivity(shareIntent);
           return true;

        }

        if (item.getItemId() == R.id.vacationstartalarm){
            try {
                Date startDateParsed = sdf.parse(editStartDate.getText().toString());
                long trigger = startDateParsed.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", editTitle.getText().toString() + " starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent alarmIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(alarmIntent);
                } else alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger,sender);

                Toast.makeText(VacationDetails.this, "Alarm Set", Toast.LENGTH_LONG).show();

                return true;
            } catch (ParseException e) {
                Toast.makeText(VacationDetails.this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        if (item.getItemId() == R.id.vacationendalarm){
            try {
                Date endDateParsed = sdf.parse(editEndDate.getText().toString());
                long trigger = endDateParsed.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", editTitle.getText().toString() + " ends today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent alarmIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(alarmIntent);
                } else alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger,sender);

                Toast.makeText(VacationDetails.this, "Alarm Set", Toast.LENGTH_LONG).show();

                return true;
            } catch (ParseException e) {
                Toast.makeText(VacationDetails.this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        if (item.getItemId() == R.id.printtopdf){
            PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            String jobName = getString((R.string.app_name)) + " Document";

            PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
                @Override
                public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
                    if (cancellationSignal.isCanceled()) {
                        callback.onLayoutCancelled();
                        return;
                    }

                    PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("vacation_details.pdf")
                            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                            .build();
                    callback.onLayoutFinished(pdi, true);
                }
                @Override
                public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
                    PdfDocument pdfDocument = new PdfDocument();
                    try {
                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                        Canvas canvas = page.getCanvas();
                        Paint paint = new Paint();
                        paint.setColor(Color.BLACK);
                        paint.setTextSize(14);

                        String message = createVacationDetails();
                        String[] lines = message.split("\n");

                        float yPosition = 40;
                        for (String line : lines) {
                            canvas.drawText(line, 25, yPosition, paint);
                            yPosition += paint.descent() - paint.ascent();
                        }

                        pdfDocument.finishPage(page);

                        pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                    } catch (Exception e) {
                        Log.e("PrintAdapter", "Error writing document: " + e, e);
                        callback.onWriteFailed(e.getMessage());
                    } finally {
                        pdfDocument.close();
                    }

                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                }
            };

            printManager.print(jobName, printAdapter, null);
            return true;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }


    private String createVacationDetails(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        StringBuilder excursionsFormatted = new StringBuilder();

        for (Excursion e: repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) {
                excursionsFormatted.append("Excursion Name: ").append(e.getExcursionName()).append("\n");
                excursionsFormatted.append("Excursion Date: ").append(sdf.format(e.getExcursionDate())).append("\n");
                excursionsFormatted.append("\n");
            }
        }

        return "Vacation Title: " + title + "\n"
                + "Hotel: " + hotel + "\n"
                + "Start Date: " + formattedStartDate + "\n"
                + "End Date: " + formattedEndDate + "\n"
                + "Your Activities:\n"
                + excursionsFormatted;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (searchView != null){
            searchView.onActionViewCollapsed();
            searchView.setQuery("", false);
        }
    }

}