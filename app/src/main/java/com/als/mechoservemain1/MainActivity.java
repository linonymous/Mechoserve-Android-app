package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    SharedPreferences prefs;

    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
    private static final String LOGIN_URL = "http://linonymo.5gbfree.com/login.php";

    // Edit Texts
    EditText editTextUserName;
    EditText editTextPassword;

    //Text Views
    TextView textViewForgotPassword;
    TextView textViewSignUp;

    Button buttonLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUserName=(EditText)findViewById(R.id.username);
        editTextPassword=(EditText)findViewById(R.id.password);

        textViewForgotPassword=(TextView)findViewById(R.id.forgotpassword);
        textViewSignUp=(TextView)findViewById(R.id.signup);

        buttonLogin=(Button)findViewById(R.id.login);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = getSharedPreferences("MYKEY",0);
        boolean savelogin=prefs.getBoolean("save",false);
        if(savelogin==true){
            Intent intent = new Intent(MainActivity.this,UserProfile.class);
            startActivity(intent);
            finish();
        }
        // Button onClick Listsner
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        // Signup TextView Listsener

        // ForgotPassword TextView Listsner

    }

    public void onClickSignUp(View v){
        Intent myintent = new Intent(MainActivity.this,Register.class);
        startActivity(myintent);
        //Toast.makeText(getApplicationContext(),"Want to sign Up?",Toast.LENGTH_LONG).show();
    }

    private void login(){
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userLogin(username, password);
    }


    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("save",true);
                    editor.putString("user", username);
                    editor.putString("pass",password);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,UserProfile.class);
                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
