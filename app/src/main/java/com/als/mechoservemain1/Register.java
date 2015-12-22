package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    //private Toolbar toolbar;
    SharedPreferences prefs;
    EditText editTextUsername;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextPhoneNo;

    Button buttonSignUp;

    public static final String USER_NAME = "USER_NAME";

    final String REGISTER_URL="http://linonymo.5gbfree.com/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextName=(EditText)findViewById(R.id.register_name);
        editTextEmail=(EditText)findViewById(R.id.register_email);
        editTextUsername=(EditText)findViewById(R.id.register_username);
        editTextPassword=(EditText)findViewById(R.id.register_password);
        editTextPhoneNo=(EditText)findViewById(R.id.register_phoneno);

        buttonSignUp=(Button)findViewById(R.id.buttonRegister);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        prefs = getSharedPreferences("MYKEY", 0);
        boolean savelogin=prefs.getBoolean("save", false);
    }

    private void registerUser(){
        String name=editTextName.getText().toString().trim().toLowerCase();
        String username=editTextUsername.getText().toString().trim().toLowerCase();
        String email=editTextEmail.getText().toString().trim().toLowerCase();
        String password=editTextPassword.getText().toString().trim().toLowerCase();
        String phoneno=editTextPhoneNo.getText().toString().trim().toLowerCase();
        if(isEmailValid(email)){
            if(isNumeric(phoneno)) {
                register(name, username, email, password, phoneno);
            }else{
                Toast.makeText(Register.this,"Phone No. Is Not Valid!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(Register.this,"Email Is Not Valid!", Toast.LENGTH_LONG).show();
        }
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

    private void register(final String name,final String username,final String email,final String password,final String phoneno){
        class RegisterUser extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")) {
                    Toast.makeText(Register.this, s, Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("save",true);
                    editor.putString("user", username);
                    editor.putString("pass", password);
                    editor.commit();
                    Intent intent = new Intent(Register.this, UserProfile.class);
                    intent.putExtra(USER_NAME, username);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Register.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("name",params[0]);
                data.put("username",params[1]);
                data.put("password",params[2]);
                data.put("email",params[3]);
                data.put("phone",params[4]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }
        RegisterUser ru = new RegisterUser();
        ru.execute(name,username,password,email,phoneno);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            registerUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
