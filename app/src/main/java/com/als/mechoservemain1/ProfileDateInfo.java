package com.als.mechoservemain1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileDateInfo extends AppCompatActivity {

    Button btn;

    Button DateB,TimeB;
    int month_x,year_x,date_x,hour_x,minute_x;
    static final int DIALOG_ID=0,DIALOG_TID=1;

    EditText timee;
    EditText datee;
    String date,time;
    String id;
    private static final String LOGIN_URL = "http://linonymo.5gbfree.com/date.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_date_info);
        btn=(Button)findViewById(R.id.proceedbtnd);
        final Calendar cal= Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        date_x=cal.get(Calendar.DAY_OF_MONTH);

        timee=(EditText)findViewById(R.id.edittime);
        datee=(EditText)findViewById(R.id.editdate);
        timee.setFocusable(false);
        timee.setFocusableInTouchMode(false);
        timee.setClickable(false);
        datee.setFocusable(false);
        datee.setFocusableInTouchMode(false);
        datee.setClickable(false);
        showDialogOnButtonClick();
        showTimePickerDialog();
        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        id=sp.getString("id", "NULL");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDate();
            }
        });
    }

    public void showTimePickerDialog(){
        TimeB=(Button)findViewById(R.id.Time1);
        TimeB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_TID);

                    }
                }
        );
    }

    public void showDialogOnButtonClick()
    {
        DateB=(Button)findViewById(R.id.Date1);
        DateB.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener , year_x, month_x, date_x);
        if(id == DIALOG_TID) {
            return new TimePickerDialog(ProfileDateInfo.this , kTimepickerListner ,hour_x ,minute_x, false);
        }
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimepickerListner=
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    hour_x= hourOfDay;
                    minute_x=minute;
                    Toast.makeText(ProfileDateInfo.this ,hour_x + " : " +minute_x,Toast.LENGTH_LONG).show();
                    timee.setText(hour_x + " : " + minute_x);
                    time=hour_x+" : "+minute_x;
                }
            };

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x= year;
            month_x= monthOfYear + 1;
            date_x= dayOfMonth;
            Toast.makeText(ProfileDateInfo.this,year_x+" / "+month_x+" / "+date_x, Toast.LENGTH_LONG).show();
            datee.setText(year_x + " / " + month_x + " / " + date_x);
            date=year_x+"-"+month_x+"-"+date_x;
        }
    };

    private void updateDate(){
        String sdate=date;
        String stime=time;

        setDate(id,sdate, stime);
    }

    private void setDate(final String id,final String sdate, final String stime){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileDateInfo.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(ProfileDateInfo.this, s, Toast.LENGTH_LONG).show();
                    Intent myintent=new Intent(ProfileDateInfo.this,ProfileFinalInfo.class);
                    startActivity(myintent);
                }else{
                    Toast.makeText(ProfileDateInfo.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("date",params[1]);
                data.put("time",params[2]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(id,sdate, stime);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_date_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            updateDate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
