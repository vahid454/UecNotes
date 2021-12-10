package com.uec.notes.uecnotes;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ElectricalActivity  extends AppCompatActivity {
    private LinearLayout parent;
    private ListView list;
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ee_activity);
        parent=findViewById(R.id.ee_layout);

        list=new ListView(ElectricalActivity.this);
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
                Toast.makeText(ElectricalActivity.this,"OPENING "+o,Toast.LENGTH_LONG).show();

                if(o.equals(l.get(0))){
                    //  Toast.makeText(ComputerScienceActivity.this,"OPENING "+o,Toast.LENGTH_LONG).show();

                }

            }
        });



    }
}
