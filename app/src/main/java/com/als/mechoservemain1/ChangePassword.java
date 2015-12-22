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

public class ChangePassword extends AppCompatActivity {

    public final String MURL="http://linonymo.5gbfree.com/temp.php";

    public final String CHANGE_URL="http://linonymo.5gbfree.com/password.php";
    EditText oldpass;
    EditText newpass;
    EditText confirmpass;

    String user;
    String pass;
    String id;

    Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldpass=(EditText)findViewById(R.id.prevpass);
        newpass=(EditText)findViewById(R.id.newpass);
        confirmpass=(EditText) findViewById(R.id.confirmpass);

        change=(Button)findViewById(R.id.changepass);
        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        user=sp.getString("user", "NULL");
        pass=sp.getString("pass","NULL");
        retrieve(user,pass);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old=oldpass.getText().toString().trim();
                String newp=newpass.getText().toString().trim();
                String conf=confirmpass.getText().toString().trim();
                if(newp.equals(conf)){
                    changePass(id,old,conf);
                    Intent myintent1=new Intent(ChangePassword.this,UserProfile.class);
                    startActivity(myintent1);
                }else{
                    Toast.makeText(ChangePassword.this, "Password Doesn't Match!" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void changePass(final String id,final String password,final String newpass) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ChangePassword.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equalsIgnoreCase("success")) {
                    Toast.makeText(ChangePassword.this, s, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChangePassword.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("id",params[0]);
                data.put("password", params[1]);
                data.put("newpass", params[2]);

                String result = ruc.sendPostRequest(CHANGE_URL, data);

                return result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(id, password, newpass);
    }

    private void retrieve(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ChangePassword.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    //Intent intent = new Intent(Profile.this,UserProfile.class);
                    //startActivity(intent);
                }else{
                    //Toast.makeText(ChangePassword.this, s, Toast.LENGTH_LONG).show();
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
        //Toast.makeText(Profile.this, qrr[0], Toast.LENGTH_LONG).show();
    }
}
