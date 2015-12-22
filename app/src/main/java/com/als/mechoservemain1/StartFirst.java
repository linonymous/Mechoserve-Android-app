package com.als.mechoservemain1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StartFirst extends AppCompatActivity {

    Button next;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_first);

        next=(Button)findViewById(R.id.startfirstnext);

        prefs = getSharedPreferences("MYKEY",0);
        boolean isStart=prefs.getBoolean("isStart",false);

        if(isStart==true){
            Intent myintent=new Intent(StartFirst.this,MainActivity.class);
            startActivity(myintent);
            finish();
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isStart",true);
                editor.commit();
                Intent myintent=new Intent(StartFirst.this,StartSecond.class);
                startActivity(myintent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_first, menu);
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
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isStart",true);
            editor.commit();
            Intent myintent=new Intent(StartFirst.this,StartSecond.class);
            startActivity(myintent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
