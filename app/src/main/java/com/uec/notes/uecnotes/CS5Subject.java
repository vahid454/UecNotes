package com.uec.notes.uecnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class CS5Subject extends AppCompatActivity {

    private LinearLayout parent;
    private ListView list;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs5_subject);
        parent=findViewById(R.id.cs5subject_layout);

        list=new ListView(CS5Subject.this);
        list.setBackgroundColor(Color.parseColor("#80d9b2"));


        String homes[]=getResources().getStringArray(R.array.CS5Subjects);

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
                // Toast.makeText(ComputerScienceActivity.this,"OPENING "+o,Toast.LENGTH_LONG).show();

                if(o.equals(l.get(0))){
                    Intent i=new Intent(CS5Subject.this,CS5SE.class);
                    startActivity(i);
                }

                if(o.equals(l.get(1))){
                    Intent i=new Intent(CS5Subject.this,CS5TOC.class);
                    startActivity(i);
                }

                if(o.equals(l.get(2))){
                    Intent i=new Intent(CS5Subject.this,CS5DC.class);
                    startActivity(i);
                }

                if(o.equals(l.get(3))){
                    Intent i=new Intent(CS5Subject.this,CS5DBMS.class);
                    startActivity(i);
                }

                if(o.equals(l.get(4))){
                    Intent i=new Intent(CS5Subject.this,CS5MI.class);
                    startActivity(i);
                }

            }
        });



    }
}