package com.uec.notes.uecnotes;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class ComputerScienceActivity  extends AppCompatActivity {
private LinearLayout parent;
private ListView list;
private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cs_activity);
              parent=findViewById(R.id.cs_layout);

        list=new ListView(ComputerScienceActivity.this);
        list.setBackgroundColor(Color.parseColor("#80d9b2"));

        String homes[]={"1st Sem","2nd Sem","3rd Sem","4rt Sem","5th Sem","6th Sem","7th Sem","8th Sem"};
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
                    Intent i=new Intent(ComputerScienceActivity.this,CS1Subject.class);
                    startActivity(i);
                }

                if(o.equals(l.get(1))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS2Subject.class);
                    startActivity(i);
                }

                if(o.equals(l.get(2))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS3Subject.class);
                    startActivity(i);
                }

                if(o.equals(l.get(3))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS4Subject.class);
                    startActivity(i);
                }

                if(o.equals(l.get(4))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS5Subject.class);
                    startActivity(i);
                }
                if(o.equals(l.get(5))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS6Subject.class);
                    startActivity(i);
                }
                if(o.equals(l.get(6))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS7Subject.class);
                    startActivity(i);
                }
                if(o.equals(l.get(7))){
                    Intent i=new Intent(ComputerScienceActivity.this,CS8Subject.class);
                    startActivity(i);
                }

            }
        });



    }
    }
