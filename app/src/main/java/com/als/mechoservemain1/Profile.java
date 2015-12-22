package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    public final String MURL="http://linonymo.5gbfree.com/temp.php";
    EditText name;
    EditText email;
    EditText phoneno;

    Button update;

    String user;
    String pass;
    String id;
    public final String UPDATE_URL="http://linonymo.5gbfree.com/update.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        phoneno=(EditText) findViewById(R.id.phoneno);

        update=(Button)findViewById(R.id.update0);


        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        user=sp.getString("user", "NULL");
        pass=sp.getString("pass","NULL");

        retrieve(user,pass);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(user, pass, name.getText().toString().trim().toLowerCase(), email.getText().toString().trim().toLowerCase(), phoneno.getText().toString().trim().toLowerCase());
                Intent myintent1=new Intent(Profile.this,UserProfile.class);
                startActivity(myintent1);
            }
        });
    }

    private void updateProfile(final String username,final String password,final String name,final String email,final String phoneno) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(Profile.this, s, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Profile.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("id",id);
                data.put("name", params[2]);
                data.put("username", params[0]);
                data.put("password", params[1]);
                data.put("email", params[3]);
                data.put("phoneno", params[4]);

                String result = ruc.sendPostRequest(UPDATE_URL, data);

                return result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute( username, password,name, email, phoneno);
    }

    private void retrieve(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Profile.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    //Intent intent = new Intent(Profile.this,UserProfile.class);
                    //startActivity(intent);
                }else{
                    Toast.makeText(Profile.this, "Change Profile!", Toast.LENGTH_LONG).show();
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
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        email.setClickable(false);
        phoneno.setText(arr[3]);
        //Toast.makeText(Profile.this, qrr[0], Toast.LENGTH_LONG).show();
    }
}
