package com.uec.notes.uecnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CS2ED extends AppCompatActivity {

    private DatabaseReference reff;
    private LinearLayout parent;
    private ListView list;
    private List<UploadPDF> uploadPDFS;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> l;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs2_ed);
        progressDialog=new ProgressDialog(this);
        parent=(LinearLayout) findViewById(R.id.cs2ed_layout);
        list=new ListView(CS2ED.this);
        list.setBackgroundColor(Color.parseColor("#80d9b2"));

        //list=(ListView) findViewById(R.id.list);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        uploadPDFS=new ArrayList<>();
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        ViewAllFiles();


        final ProgressDialog pd=new ProgressDialog(CS2ED.this);
        pd.setMessage("Deleting....");
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Object o = list.getItemAtPosition(i);
                UploadPDF uploadPDF=uploadPDFS.get(i);


                final String filename = (String) uploadPDF.getName().toString();

                if (user != null) {


                    new AlertDialog.Builder(CS2ED.this).setTitle("Delete " + filename + " ?").setMessage("Do you really want to delete this file?").setIcon(android.R.drawable.ic_dialog_dialer).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd.show();
                            reff = FirebaseDatabase.getInstance().getReference("uploads").child("CS").child("CS2").child("CS2ED").child(filename.trim());

                            StorageReference sr = FirebaseStorage.getInstance().getReference("CS/CS2/CS2ED" + filename.trim() + ".pdf");
                            sr.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            reff.removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(CS2ED.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                                                            pd.dismiss();
                                                            finish();
                                                            startActivity(getIntent());




                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.cancel();
                                    Toast.makeText(CS2ED.this, "File cannot be deleted.", Toast.LENGTH_LONG).show();

                                }
                            });


                        }
                    }).setNegativeButton(android.R.string.no, null).show();

                }else
                {
                    Toast.makeText(CS2ED.this,"You are not a teacher",Toast.LENGTH_LONG).show();
                    return true;
                }
                return true;

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadPDF uploadPDF=uploadPDFS.get(i);
                Intent intent=new Intent();
                // intent.setType(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_QUICK_VIEW);
                intent.setData(Uri.parse(uploadPDF.getUrl()));
                startActivity(intent);
            }
        });

    }



    private void ViewAllFiles() {
        reff=FirebaseDatabase.getInstance().getReference("uploads").child("CS").child("CS2").child("CS2ED");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    UploadPDF uploadPDF=postSnapshot.getValue(UploadPDF.class);
                    uploadPDFS.add(uploadPDF);

                }

                final ArrayList<String> l=new ArrayList<String>();
                for(int i=0;i<uploadPDFS.size();i++)
                {
                    l.add(uploadPDFS.get(i).getName());
                }
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(CS2ED.this,android.R.layout.simple_expandable_list_item_1,l);


                list.setAdapter(arrayAdapter);

                if(l.size()==0)
                {
                    TextView t=new TextView(CS2ED.this);
                    t.setTextSize(24);
                    t.setTextColor(Color.parseColor("#80d9b2"));
                    t.setText(R.string.no_files);
                    t.setGravity(1);
                    parent.addView(t);
                }
                parent.addView(list);
                progressDialog.cancel();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

