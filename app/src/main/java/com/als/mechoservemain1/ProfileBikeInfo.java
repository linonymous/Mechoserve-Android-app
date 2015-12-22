package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileBikeInfo extends AppCompatActivity {

    EditText company;
    EditText modelno;
    EditText yop;
    EditText vno;

    private static final String LOGIN_URL = "http://linonymo.5gbfree.com/bike.php";

    Button btn;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_bike_info);
        company=(EditText)findViewById(R.id.edittextbikecompany);
        modelno=(EditText)findViewById(R.id.edittextmodelno);
        yop=(EditText)findViewById(R.id.edittextyear);
        vno=(EditText)findViewById(R.id.edittextvehicleno);
        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        id=sp.getString("id", "NULL");
        btn=(Button)findViewById(R.id.proceedbtnb);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBikeDetails();
            }
        });
    }

    private void updateBikeDetails(){
        String scompany=company.getText().toString().trim();
        String smodelno=modelno.getText().toString().trim();
        String syop=yop.getText().toString().trim();
        String svno=vno.getText().toString().trim();

        finalUpdate(id,scompany, smodelno, syop, svno);
    }

    private void finalUpdate(final String id,final String scompany, final String smodelno,final String syop,final String svno){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileBikeInfo.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(ProfileBikeInfo.this, s, Toast.LENGTH_LONG).show();
                    Intent myintent=new Intent(ProfileBikeInfo.this,ProfileDateInfo.class);
                    startActivity(myintent);
                }else{
                    Toast.makeText(ProfileBikeInfo.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("company",params[1]);
                data.put("modelno",params[2]);
                data.put("yop",params[3]);
                data.put("vno",params[4]);
                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(id,scompany,smodelno,syop,svno);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_bike_info, menu);
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
            updateBikeDetails();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
