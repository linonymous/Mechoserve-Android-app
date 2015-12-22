package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfilePersonalInfo extends AppCompatActivity {

    Button proceed;
    public final String MURL="http://linonymo.5gbfree.com/temp.php";
    EditText name;
    EditText email;
    EditText phone;
    String id;

    String user;
    String pass;
    private static final String LOGIN_URL = "http://linonymo.5gbfree.com/personal.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_personal_info);
        proceed=(Button)findViewById(R.id.proceedbtn);

        name=(EditText)findViewById(R.id.edittextname);
        email=(EditText)findViewById(R.id.edittextemail);
        phone=(EditText)findViewById(R.id.edittextcontact);
        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        id=sp.getString("id", "NULL");
        user=sp.getString("user", "NULL");
        pass=sp.getString("pass","NULL");
        retrieve(user, pass);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPersonalInfo();
            }
        });
    }


    private void retrieve(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfilePersonalInfo.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    //Intent intent = new Intent(Profile.this,UserProfile.class);
                    //startActivity(intent);
                }else{
                    Toast.makeText(ProfilePersonalInfo.this,"Welcome "+user+" !!", Toast.LENGTH_LONG).show();
                    getData(s);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(MURL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }


    public void getData(String s){
        String []arr=s.split("#");
        id=arr[0];
        name.setText(arr[1]);
        email.setText(arr[2]);
        phone.setText(arr[3]);
        //Toast.makeText(Profile.this, qrr[0], Toast.LENGTH_LONG).show();
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return (str.length()<13);
    }

    private void setPersonalInfo(){
        String nameEdit=name.getText().toString().trim();
        String emailEdit=email.getText().toString().trim();
        String phoneEdit=phone.getText().toString().trim();
        if(isEmailValid(emailEdit)) {
            if(isNumeric(phoneEdit)) {
                updateDatabase(id,nameEdit, emailEdit, phoneEdit);
            }else{
                Toast.makeText(ProfilePersonalInfo.this,"Phone No. is not Valid",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(ProfilePersonalInfo.this,"Email is not Valid",Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void updateDatabase(final String id,final String nameEdit, final String emailEdit,final String phoneEdit){
        class UpdateClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfilePersonalInfo.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(ProfilePersonalInfo.this,"PROCEED", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfilePersonalInfo.this,ProfileAddressInfo.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ProfilePersonalInfo.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("name",params[1]);
                data.put("email",params[2]);
                data.put("phone",params[3]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UpdateClass ulc = new UpdateClass();
        ulc.execute(id,nameEdit, emailEdit,phoneEdit);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_personal_info, menu);
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
            setPersonalInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
