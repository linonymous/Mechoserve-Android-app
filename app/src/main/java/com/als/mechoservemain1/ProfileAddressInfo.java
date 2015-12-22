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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class ProfileAddressInfo extends AppCompatActivity {

    Button btn;

    private static final String LOGIN_URL = "http://linonymo.5gbfree.com/address.php";

    EditText addr;
    EditText pincode;
    EditText region;
    EditText state;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_address_info);
        addr=(EditText)findViewById(R.id.edittextaddr);
        pincode=(EditText)findViewById(R.id.edittextpincode);
        region=(EditText) findViewById(R.id.edittextregion);
        state=(EditText) findViewById(R.id.edittextstate);

        SharedPreferences sp = getSharedPreferences("MYKEY",0);
        id=sp.getString("id", "NULL");
        btn=(Button)findViewById(R.id.proceedbtna);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAddress();
            }
        });
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
        return (str.length()<7);
    }

    private void updateAddress(){
        String saddr=addr.getText().toString().trim();
        String spincode=pincode.getText().toString().trim();
        String sregion=region.getText().toString().trim();
        String sstate=state.getText().toString().trim();
        if(isNumeric(spincode)){
            updateIt(id,saddr, spincode, sregion, sstate);
        }else{
            Toast.makeText(ProfileAddressInfo.this,"Pincode is not Valid",Toast.LENGTH_LONG).show();
        }
    }


    private void updateIt(final String id,final String saddr, final String spincode, final String sregion, final String sstate){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ProfileAddressInfo.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Toast.makeText(ProfileAddressInfo.this, s, Toast.LENGTH_LONG).show();
                    Intent myintent=new Intent(ProfileAddressInfo.this,ProfileBikeInfo.class);
                    startActivity(myintent);
                }else{
                    Toast.makeText(ProfileAddressInfo.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("id",params[0]);
                data.put("addr",params[1]);
                data.put("pincode",params[2]);
                data.put("region",params[3]);
                data.put("state",params[4]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);
                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(id,saddr, spincode,sregion,sstate);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_address_info, menu);
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
            updateAddress();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

