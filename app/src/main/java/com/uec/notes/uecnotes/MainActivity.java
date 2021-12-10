package com.uec.notes.uecnotes;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
private LinearLayout parent;
private ListView list;
private ArrayAdapter<String> arrayAdapter;
private FirebaseAuth firebaseAuth;
private FirebaseUser user;
private DatabaseReference reff;

private String email,nameOfActiveUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        reff=FirebaseDatabase.getInstance().getReference().child("Teacher");

        if(user!=null)
        {
           email=user.getEmail();
          // String key=reff.getKey(); //Teacher
          // Object o=reff.getDatabase(); //faltu




        }



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        parent=(LinearLayout) findViewById(R.id.parent);

      //  list=(ListView) findViewById(R.id.list);

        list=new ListView(MainActivity.this);

        list.setBackgroundColor(Color.parseColor("#80d9b2"));

        String homes[]={"Computer Science","Electronics & Communication","Electrical Engineering","Mechanical Engineering","Civil Engineering","Chemical Engineering"};
        final ArrayList<String> l=new ArrayList<String>();
        for(int i=0;i<homes.length;i++)
        {
            l.add(homes[i]);
        }
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,l);


        list.setAdapter(arrayAdapter);
      parent.addView(list);






        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object o=list.getItemAtPosition(position);
                if(o.equals(l.get(0))){

                    Intent i=new Intent(MainActivity.this,ComputerScienceActivity.class);
                    startActivity(i);
                }
                if(o.equals(l.get(1))) {
                    Intent i = new Intent(MainActivity.this, ECActivity.class);
                    startActivity(i);
                }
                if(o.equals(l.get(2))) {
                    Intent i = new Intent(MainActivity.this, ElectricalActivity.class);
                    startActivity(i);
                }
                if(o.equals(l.get(3))){
                    Intent i=new Intent(MainActivity.this,MechanicalActivity.class);
                    startActivity(i);
                }
                if(o.equals(l.get(4))){
                    Intent i=new Intent(MainActivity.this,CivilActivity.class);
                    startActivity(i);
                }
                if(o.equals(l.get(5))){
                    Intent i=new Intent(MainActivity.this,ChemicalActivity.class);
                    startActivity(i);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Toast.makeText(this,"Setting",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
       // Toast.makeText(this,"NAV - "+item.getItemId(),Toast.LENGTH_LONG).show();
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Intent i=new Intent(MainActivity.this,Gallery.class);
            startActivity(i);

        }else if (id==R.id.nav_teacher) {
            if(user!=null)
            {




                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            Teacher teacher=ds.getValue(Teacher.class);
                            if(email.equalsIgnoreCase(teacher.getEmail()))
                            {
                                nameOfActiveUser=teacher.getName();
                                Intent i=new Intent(MainActivity.this,TeacherMainDashboard.class);
                                i.putExtra("email",email);
                                i.putExtra("name",nameOfActiveUser);
                                i.putExtra("branch",teacher.getBranch().toString());
                                System.out.println("NAMEFROM"+nameOfActiveUser+"BRANCH"+teacher.getBranch());
                                startActivity(i);

                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }else
            {

                Intent i=new Intent(MainActivity.this,TeacherLoginActivity.class);
                startActivity(i);
                //login as teacher
            }


        } else if (id == R.id.nav_share) {
            Toast.makeText(this,"Share",Toast.LENGTH_LONG).show();
        } else if (id==R.id.nav_exit) {
            new AlertDialog.Builder(MainActivity.this).setTitle("Exit Alert!!").setMessage("Do you really want to exit?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            }).setNegativeButton(android.R.string.no,null).show();
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
