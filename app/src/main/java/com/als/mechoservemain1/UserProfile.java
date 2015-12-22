package com.als.mechoservemain1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity {

    public final String MURL="http://linonymo.5gbfree.com/temp.php";
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    Button book;

    String user;
    String pass;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        book=(Button)findViewById(R.id.bookappointment);
        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        SharedPreferences sp = getSharedPreferences("MYKEY", 0);
        user=sp.getString("user", "NULL");
        pass=sp.getString("pass","NULL");
        retrieve(user, pass);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent3 = new Intent(UserProfile.this, ProfilePersonalInfo.class);
                startActivity(myintent3);
            }
        });
    }

    private void retrieve(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UserProfile.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("success")){
                    //Intent intent = new Intent(Profile.this,UserProfile.class);
                    //startActivity(intent);
                }else{
                    Toast.makeText(UserProfile.this,"Welcome "+user+" !!", Toast.LENGTH_LONG).show();
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
        SharedPreferences sp = getSharedPreferences("MYKEY", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id", id);
        editor.commit();
        //Toast.makeText(Profile.this, qrr[0], Toast.LENGTH_LONG).show();
    }

    private void addDrawerItems() {
        String[] osArray = { "Profile", "Change Password", "Refer a Friend", "Share",  "Services" , "How It Works?","Contact Us", "Logout"  };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
                //Toast.makeText(MainActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void displayView(int position){
        switch(position){
            case 0:
                SharedPreferences sp = getSharedPreferences("MYKEY",0);
                String user=sp.getString("user", "NULL");
                String pass=sp.getString("pass","NULL");
                //Toast.makeText(UserProfile.this, user+" "+pass , Toast.LENGTH_SHORT).show();
                Intent myintent3=new Intent(UserProfile.this,Profile.class);
                startActivity(myintent3);
                break;
            case 1:
                Toast.makeText(UserProfile.this, "Change Password", Toast.LENGTH_SHORT).show();
                Intent myintent4=new Intent(UserProfile.this,ChangePassword.class);
                startActivity(myintent4);
                break;
            case 2:
                Toast.makeText(UserProfile.this, "Refer a Friend", Toast.LENGTH_SHORT).show();
                Intent sharingIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent1.setType("text/plain");
                String shareBody1 = "Hey There! I found an amazing app MECHOSERVE, Get Your Bike Serviced at Your Home!";
                sharingIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent1.putExtra(android.content.Intent.EXTRA_TEXT, shareBody1);
                startActivity(Intent.createChooser(sharingIntent1, "Tell Your Friends Via"));
                break;
            case 3:
                Toast.makeText(UserProfile.this, "Share", Toast.LENGTH_SHORT).show();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey There! I found an amazing app MECHOSERVE, Get Your Bike Serviced at Your Home!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                break;
            case 4:
                Toast.makeText(UserProfile.this, "Services", Toast.LENGTH_SHORT).show();
                Intent myintent=new Intent(UserProfile.this,TestActivity.class);
                startActivity(myintent);
                break;
            case 5:
                Toast.makeText(UserProfile.this, "How It Works!", Toast.LENGTH_SHORT).show();
                Intent myintent1=new Intent(UserProfile.this,HowItWorks.class);
                startActivity(myintent1);
                break;
            case 6:
                Toast.makeText(UserProfile.this, "Contact Us", Toast.LENGTH_SHORT).show();
                Intent myintent2=new Intent(UserProfile.this,ContactUs.class);
                startActivity(myintent2);
                break;
            case 7:
                Toast.makeText(UserProfile.this, "Logout", Toast.LENGTH_SHORT).show();
                SharedPreferences spa = getSharedPreferences("MYKEY",0);
                SharedPreferences.Editor editor = spa.edit();
                editor.putBoolean("save", false);
                editor.commit();
                Intent in=new Intent(UserProfile.this,MainActivity.class);
                startActivity(in);
                finish();
                break;
        }
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Explore");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        //mDrawerLayout.openDrawer(containerView);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerLayout.post(new Runnable() {
        // @Override
        // public void run() {

        //}
        //});
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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
            SharedPreferences sp = getSharedPreferences("MYKEY",0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("save", false);
            editor.commit();
            Intent in=new Intent(UserProfile.this,MainActivity.class);
            startActivity(in);
            finish();
        }

        if( mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
