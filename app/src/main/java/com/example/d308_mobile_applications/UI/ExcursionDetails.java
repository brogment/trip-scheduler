package com.example.d308_mobile_applications.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_mobile_applications.R;
import com.example.d308_mobile_applications.database.Repository;
import com.example.d308_mobile_applications.entities.Excursion;
import com.example.d308_mobile_applications.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    int excursionID;
    int vacationID;
    long excursionDate;
    EditText editName;
    TextView editDate;
    Repository repository;
    Excursion currentExcursion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository=new Repository(getApplication());

        editName = findViewById(R.id.excursionname);
        editDate = findViewById(R.id.excursiondate);

        name = getIntent().getStringExtra("name");
        excursionDate = getIntent().getLongExtra("date", -1);
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacaID", -1);

        //date validation
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        String formattedDate = (excursionDate != -1) ? sdf.format(new Date(excursionDate)) : "Excursion date not set";

        editName.setText(name);
        editDate.setText(formattedDate);

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editDate);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
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

        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;

            try {
                Date excursionDateParsed = sdf.parse(editDate.getText().toString());
                Vacation currentVacation = repository.getVacationByID(vacationID);

                if (currentVacation != null) {
                    Date vacationStartDate = currentVacation.getVacationStartDate();
                    Date vacationEndDate = currentVacation.getVacationEndDate();

                    if (!excursionDateParsed.before(vacationStartDate) && !excursionDateParsed.after(vacationEndDate)) {
                        if (excursionID == -1) {

                            if (repository.getAllExcursions().size() == 0) excursionID = 1;
                            else excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;

                            excursion = new Excursion(excursionID, editName.getText().toString(), excursionDateParsed, vacationID);
                            repository.insert(excursion);
                            return true;
                        } else {
                            excursion = new Excursion(excursionID, editName.getText().toString(), excursionDateParsed, vacationID);
                            repository.update(excursion);
                            return true;
                        }
                    } else {
                        Toast.makeText(ExcursionDetails.this, "Excursion must occur during vacation", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
            } catch (ParseException e) {
                Toast.makeText(ExcursionDetails.this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;
            }

        }

        if(item.getItemId()== R.id.excursiondelete){
            for(Excursion excursion:repository.getAllExcursions()){
                if(excursion.getExcursionID() == excursionID) currentExcursion = excursion;
            }
            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            ExcursionDetails.this.finish();
        }

        if (item.getItemId() == R.id.excursionalert){
            try {
                Date excursionDateParsed = sdf.parse(editDate.getText().toString());
                long trigger = excursionDateParsed.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", editName.getText().toString() + " is today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent alarmIntent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(alarmIntent);
                } else alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger,sender);

                Toast.makeText(ExcursionDetails.this, "Alarm Set", Toast.LENGTH_LONG).show();

                return true;
            } catch (ParseException e) {
                Toast.makeText(ExcursionDetails.this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }



}