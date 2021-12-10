package com.uec.notes.uecnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class TeacherMainDashboard extends AppCompatActivity {
private LinearLayout parent;
private FirebaseAuth firebaseAuth;
private String email,nameOfActiveUser;
private ProgressDialog progressDialog;
private EditText nameOfFile;
private Spinner semSpinner;
//private Spinner branchSpinner;
private String sName;
private int flagSem;
 private String sem="1",branch,subject_name;
 private ArrayAdapter<String> arrayAdapter,arrayAdapterBranch;
private Button uploadButton,uploadImageButton;
private StorageReference storageReference;
private DatabaseReference databaseReference;
private String branchOfActiveUser;
private  FirebaseUser user;
private TextView branchView;
private Spinner subjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main_dashboard);
        parent=findViewById(R.id.teacher_main_dashboard);
        Button signout=new Button(TeacherMainDashboard.this);
        progressDialog=new ProgressDialog(this);


        nameOfFile=(EditText) findViewById(R.id.name_of_pdf);
        branchView=(TextView) findViewById(R.id.branchView);
        uploadButton=(Button) findViewById(R.id.upload_button);
        uploadImageButton=(Button) findViewById(R.id.upload_image_button);


        Intent i = getIntent();
        email=i.getStringExtra("email");
        nameOfActiveUser=i.getStringExtra("name");
        branchOfActiveUser=i.getStringExtra("branch");
        branchView.setText(branchOfActiveUser);
        TextView nameView=new TextView(TeacherMainDashboard.this);
        nameView.setTextColor(Color.parseColor("#80d9b2"));
        nameView.setText(nameOfActiveUser);
        nameView.setTextSize(20);

        nameView.setGravity(1);
        signout.setText("Sign Out");

        //Retriving name and branch from database
  /*
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("Teacher");


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds:dataSnapshot.getChildren())
                    {
                        Teacher teacher=ds.getValue(Teacher.class);
                        if(email.equalsIgnoreCase(teacher.getEmail()))
                        {
                            nameOfActiveUser=teacher.getName();
                            branchOfActiveUser=teacher.getBranch();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        }

*/
        // uploading to firebase
        storageReference=FirebaseStorage.getInstance().getReference();
       // databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameOfFile.getText().toString().length()==0)
                {
                    Toast.makeText(TeacherMainDashboard.this,"Enter name of file first",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    SelectPDFFile();
                }

            }
        });
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameOfFile.getText().toString().length()==0)
                {
                    Toast.makeText(TeacherMainDashboard.this,"Enter name of image first",Toast.LENGTH_SHORT).show();
                    return;
                }else
                {
                    SelectIMAGEFile();
                }


            }
        });

//sem spinner
          semSpinner=findViewById(R.id.sem);
      String homes[]=getResources().getStringArray(R.array.sems);
    final ArrayList<String> l=new ArrayList<String>();
        for(int itr=0;itr<homes.length;itr++)
        {
            l.add(homes[itr]);
        }
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,l);
       semSpinner.setAdapter(arrayAdapter);

//subjects spinner
        String shomes[];
        shomes=getResources().getStringArray(R.array.CS1Subjects);
        subjects=findViewById(R.id.subjects);

        final ArrayList<String> bl=new ArrayList<String>();
        for(int itr=0;itr<shomes.length;itr++)
        {
            bl.add(shomes[itr]);
        }
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,bl);

        subjects.setAdapter(arrayAdapter);





        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj=adapterView.getItemAtPosition(i);
                sem=obj.toString();
                System.out.println("CHANGES "+sem);
                flagSem=Integer.parseInt(sem);

                subjects.setSelection(i);



                //ends
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj=adapterView.getItemAtPosition(i);
                System.out.println("TOOLS"+obj);
                subject_name=obj.toString();

                if(sem.toString().equalsIgnoreCase("1") && branchOfActiveUser.equalsIgnoreCase("CS"))
                {
                    if(obj.toString().equalsIgnoreCase("Physics"))
                    {
                        sName="CS1Physics";
                    }
                    if(obj.toString().equalsIgnoreCase("Basic Mechanical Engg."))
                    {
                        sName="CS1BME";
                    }
                    if(obj.toString().equalsIgnoreCase("Mathematics-I"))
                    {
                        sName="CS1M1";
                    }
                    if(obj.toString().equalsIgnoreCase("Basic Electrical Engg."))
                    {
                        sName="CS1BEE";
                    }
                    if(obj.toString().equalsIgnoreCase("C Language"))
                    {
                        sName="CS1CLang";
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        System.out.println("SUBJECTSS : "+subject_name);


     /*   branchSpinner=findViewById(R.id.branch);
        //spinner.setBackgroundColor(Color.parseColor("#80d9b2"));

//        String bhomes[]={branchOfActiveUser};
            String bhomes[]={"CS","EC","ME","CE","CM","EE"};
        final ArrayList<String> ll=new ArrayList<String>();
        for(int itr=0;itr<bhomes.length;itr++)
        {
            ll.add(bhomes[itr]);
        }
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,ll);


        branchSpinner.setAdapter(arrayAdapter);

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj=adapterView.getItemAtPosition(i);
                branch=obj.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

*/
       // branch=branchOfActiveUser.toString();

     //   System.out.println("UTHA LEGA"+branchOfActiveUser);
        branch=branchOfActiveUser;



        // adding to UI
        parent.addView(signout);
        parent.addView(nameView);
        signout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressDialog.setMessage("Signing Out....");
                progressDialog.show();

                FirebaseAuth.getInstance().signOut();


                Runnable progressRunnable=new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();
                        Intent i=new Intent(TeacherMainDashboard.this,MainActivity.class);

                        startActivity(i);


                    }
                };
                Handler pdHandller=new Handler();
                pdHandller.postDelayed(progressRunnable,3000);


            }
        });

    }

    private void UploadImageActivity(final Uri data) {

        storageReference=FirebaseStorage.getInstance().getReference("images");
        // databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference("images");

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(" Uploading");
        progressDialog.show();
        final String folderName=branch.toString().toUpperCase().trim()+sem.toString();

        StorageReference sr=storageReference.child(nameOfFile.getText().toString().trim()+".jpg");

        sr.putFile(data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        progressDialog.dismiss();
                        Toast.makeText(TeacherMainDashboard.this, "File Uploaded Successfully", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());

                        final Uri urlOfStorage=uri.getResult();
                        UploadImage uploadImage=new UploadImage();
                        uploadImage.setName(nameOfFile.getText().toString());
                        uploadImage.setUrl(urlOfStorage.toString());
                        databaseReference.child(uploadImage.getName()).setValue(uploadImage);


                        //  databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);




                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading : "+ (int)progress+"%");

            }
        });













    }

    private void SelectIMAGEFile() {
        Intent i=new Intent();
        i.setType("image/jpeg");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select image file to upload"),123);

    }

    private void SelectPDFFile() {
        Intent i=new Intent();
        i.setType("application/pdf");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select PDF file to upload"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {

            UploadPDFFile(data.getData());
        }
        if(requestCode==123 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {

            UploadImageActivity(data.getData());
        }
    }

    private void UploadPDFFile(final Uri data) {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(" Uploading");
        progressDialog.show();
        final String folderName=branch.toString().toUpperCase().trim()+sem.toString();


        StorageReference sr=storageReference.child(branch+"/"+folderName+"/"+sName.toString().trim()+nameOfFile.getText().toString().trim()+".pdf");

        sr.putFile(data)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        progressDialog.dismiss();
                        Toast.makeText(TeacherMainDashboard.this, "File Uploaded Successfully", Toast.LENGTH_LONG).show();

                        /*Intent it=new Intent(TeacherMainDashboard.this,TeacherMainDashboard.class);
                        it.putExtra("name",nameOfActiveUser);
                        it.putExtra("email",email);
                        it.putExtra("branch",branchOfActiveUser);
                        startActivity(it);*/

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                         while(!uri.isComplete());
                        final Uri urlOfStorage=uri.getResult();
                        UploadPDF uploadPDF=new UploadPDF();
                        uploadPDF.setName(nameOfFile.getText().toString());
                        uploadPDF.setUrl(urlOfStorage.toString());
                        uploadPDF.setSem(sem);
                        uploadPDF.setBranch(branch);
//            databaseReference.child(branch).child(folderName).child("CS1Physics").child(uploadPDF.getName()).setValue(uploadPDF);

                        databaseReference.child(branch).child(folderName).child(sName).child(uploadPDF.getName()).setValue(uploadPDF);

                        //  databaseReference.child(databaseReference.push().getKey()).setValue(uploadPDF);





                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

            double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
            progressDialog.setMessage("Uploading : "+ (int)progress+"%");



            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(TeacherMainDashboard.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
