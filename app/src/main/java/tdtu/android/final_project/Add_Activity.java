package tdtu.android.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_Activity extends AppCompatActivity {

    private EditText nameReminder;
    private EditText noteReminder;
    private EditText placeReminder;
    private EditText timeReminder;
    private EditText dateReminder;
    private Switch flagSwitch;
    private int getFlag;
    private EditText importantLevel;
    private int lastSelectedHour = -1;
    private int lastSelectedMinute = -1;

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String Date;
    private ActionBar toolbar;
    private static final int REQUEST_CODE_STOPPED = 1;
    private String timeTonotify;

    private String edit = null;
    String reminderName_edit = " ";
    int reminderCheck = -1;
    private Button editButton;
    private TextView namePersonTodoList;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private String name;
    private DatabaseReference reference = db.getReference("User");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        //toolbar
        toolbar = getSupportActionBar();
        toolbar.setTitle("Add Todo");

        nameReminder = findViewById(R.id.nameReminder);
        noteReminder = findViewById(R.id.noteReminder);
        placeReminder = findViewById(R.id.placeReminder);
        timeReminder = findViewById(R.id.timeReminder);
        dateReminder = findViewById(R.id.dateReminder);
        flagSwitch = findViewById(R.id.flagSwitch);
        importantLevel = findViewById(R.id.importantLevel);
        editButton= findViewById(R.id.editButton);
        editButton.setVisibility(View.GONE);

        importantLevel.setEnabled(false);

        //get User Name
        namePersonTodoList = findViewById(R.id.namePersonTodoList);
        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);


        name = sh.getString("name", "");

        reference.child(name).child("userName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().equals("")){
                    namePersonTodoList.setText(name);
                }
                else{
                    namePersonTodoList.setText(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // Intent to get reminder edit
        Intent getIntent = getIntent();
        reminderName_edit = getIntent.getStringExtra("key");
        reminderCheck = getIntent.getIntExtra("check", 0);
//        Toast.makeText(getApplicationContext(), reminderName_edit, Toast.LENGTH_LONG).show();

        if(reminderCheck==1){  //Edit Case
            editButton.setVisibility(View.VISIBLE);
            toolbar.hide();
            reference.child(name).child("Reminder").child(reminderName_edit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Reminder reminder = snapshot.getValue(Reminder.class);
                    if(String.valueOf(reminder.getReminderID()).equals(reminderName_edit)){
                        nameReminder.setText(reminder.getReminderName());
                        noteReminder.setText(reminder.getReminderContent());
                        placeReminder.setText(reminder.getReminderPlace());
                        timeReminder.setText(reminder.getReminderTime());
                        dateReminder.setText(reminder.getReminderDate());
                        if(reminder.getReminderImportant()>0){
                            importantLevel.setEnabled(true);
                            flagSwitch.setChecked(true);
                            importantLevel.setText(Integer.toString(reminder.getReminderImportant()));
                        }
                        if(reminder.getReminderImportant()==0){
                            importantLevel.setEnabled(false);
                            flagSwitch.setChecked(false);
                            importantLevel.setText("None");
                        }
                        editButton.setOnClickListener((View view)->{
                            Reminder reminderUpdate = new Reminder();
                            reminderUpdate.setReminderID(Integer.parseInt(reminderName_edit));
                            reminderUpdate.setReminderName(nameReminder.getText().toString());
                            reminderUpdate.setReminderContent(noteReminder.getText().toString());
                            reminderUpdate.setReminderTime(timeReminder.getText().toString());
                            reminderUpdate.setReminderDate(dateReminder.getText().toString());
                            reminderUpdate.setReminderPlace(placeReminder.getText().toString());
                            String important_Level=  importantLevel.getText().toString();
                            int level;
                            if (important_Level.equals("1")){
                                level=1;
                            }
                            else if (important_Level.equals("2")){
                                level=2;
                            }
                            else if (important_Level.equals("3")){
                                level=3;
                            }
                            else{
                                level=0;
                            }
                            reminderUpdate.setReminderImportant(level);
                            reminderUpdate.setReminderDone(0);

                            reference.child(name).child("Reminder").child(reminderName_edit).setValue(reminderUpdate);

                            // Edit case
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("EditCase", "edit");
                            myEdit.commit();

                            //go back to Home
                            Intent goToHome = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(goToHome);
                            finish();
                        });
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_LONG).show();

            }
        });

        }

        //Input
       flagSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    getFlag = 1;
                    importantLevel.setEnabled(true);
                    importantLevel.setText("1");

                } else {
                    getFlag = 0;
                    importantLevel.setText("None");
                    importantLevel.setEnabled(false);

                }
            }
        });

        String[] listLevel = new String[]{ "1", "2", "3"};
        importantLevel.setOnClickListener((View view)->{
            AlertDialog.Builder mBuilder =new AlertDialog.Builder(Add_Activity.this);
            mBuilder.setTitle("Choose your level !!!");
            mBuilder.setSingleChoiceItems(listLevel, -1, (dialogInterface, i) -> {
                importantLevel.setText(listLevel[i]);
                dialogInterface.dismiss();
            });
            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });




        // find current Date
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date = simpleDateFormat.format(calendar.getTime());

        int day =Integer.parseInt(Date.substring(0,2));
        int month = Integer.parseInt(Date.substring(3,5));
        int year = Integer.parseInt(Date.substring(6,10));

        int hour = Integer.parseInt(Date.substring(11,13));
        int minutes = Integer.parseInt(Date.substring(14,16));

        timeReminder.setOnClickListener((View view) -> {
            boolean is24HView = true;
            int selectedHour = hour;
            int selectedMinute = minutes;

            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    timeReminder.setText(hourOfDay + ":" + minute );
                    timeTonotify = hourOfDay + ":" + minute;
                    lastSelectedHour = hourOfDay;
                    lastSelectedMinute = minute;
                }
            };

            // Create TimePickerDialog:
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    timeSetListener, selectedHour, selectedMinute, is24HView);

            // Show
            timePickerDialog.show();
        });

        //input date
        dateReminder.setOnClickListener((View view)->{
            int selectedYear = year;
            int selectedMonth = month;
            int selectedDayOfMonth = day;

            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    Add_Activity.this.dateReminder.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, selectedYear, selectedMonth-1, selectedDayOfMonth);
            datePickerDialog.show();
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_second, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private int flag =1; // for Options Menu

    private void setAlarm(String text, String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   // alarm manager object to set alarm

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
//            Toast.makeText(getApplicationContext(), "Alaram", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                // Validate
                if(nameReminder.getText().toString().isEmpty() || noteReminder.getText().toString().isEmpty()
                    || timeReminder.getText().toString().isEmpty() || dateReminder.getText().toString().isEmpty()
                    || placeReminder.getText().toString().isEmpty()){
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(Add_Activity.this);
                    alertDialog.setTitle("Your Reminder is not Complete !!!");
                    alertDialog.setMessage("Please complete your Reminder ");
                    alertDialog.setNegativeButton("OK", (dialogInterface, i) -> {
                    });
                    flag = 3; // case Save
                    alertDialog.show();

                    if(dateReminder.getText().toString().isEmpty()){
                        dateReminder.setError("Please input date Reminder");
                        dateReminder.requestFocus();
                    }

                    if(timeReminder.getText().toString().isEmpty()){
                        timeReminder.setError("Please input time Reminder");
                        timeReminder.requestFocus();
                    }

                    if(placeReminder.getText().toString().isEmpty()){
                        placeReminder.setError("Please input place Reminder");
                        placeReminder.requestFocus();
                    }

                    if(noteReminder.getText().toString().isEmpty()){
                        noteReminder.setError("Please input note Reminder");
                        noteReminder.requestFocus();
                    }

                    if(nameReminder.getText().toString().isEmpty()){
                        nameReminder.setError("Please input name Reminder");
                        nameReminder.requestFocus();
                    }
                }
                else {

                    String reminderName = nameReminder.getText().toString();
                    String reminderNote = noteReminder.getText().toString();
                    String reminderTime = timeReminder.getText().toString();
                    String reminderDate = dateReminder.getText().toString();
                    String reminderPlace = placeReminder.getText().toString();
                    String important_Level=  importantLevel.getText().toString();
                    int level;
                    if (important_Level.equals("1")){
                        level=1;
                    }
                    else if (important_Level.equals("2")){
                        level=2;
                    }
                    else if (important_Level.equals("3")){
                        level=3;
                    }
                    else{
                        level=0;
                    }

                    SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    String id = sh.getString("id", "0");

//                    Add reminder to firebase
                    Reminder reminder = new Reminder(Integer.parseInt(id), reminderName, reminderNote, reminderTime, reminderDate, reminderPlace, level, 0);
                    reference.child(name).child("Reminder").child(id).setValue(reminder);
                    int idAfter = Integer.parseInt(id)+1;


                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putString("id", String.valueOf(idAfter));
                    myEdit.commit();



                    reference.child(name).child("userCount").setValue(idAfter);

//                    Toast.makeText(getApplicationContext(), Integer.toString(level), Toast.LENGTH_SHORT).show();

                    //set alarm
                    setAlarm(reminderName, reminderDate, reminderTime);

                    //go back to home activity
                    Intent Add_ActivityIntent = new Intent();

                    setResult(Activity.RESULT_OK, Add_ActivityIntent);
                    flag=1; // set default
                    finish();
                }

            case R.id.cancelButton:
                if(flag==3){ // cancel Case
                    flag=1;
                }
                else{
                    //turn back to home
                    Intent Cancel_ActivityIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, Cancel_ActivityIntent);
                    finish();
                }

            case R.id.reset:
                nameReminder.setText("");
                noteReminder.setText("");
                placeReminder.setText("");
                timeReminder.setText("");
                dateReminder.setText("");
                importantLevel.setText("None");
                flagSwitch.setChecked(false);

            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
