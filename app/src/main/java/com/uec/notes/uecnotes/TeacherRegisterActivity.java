package com.uec.notes.uecnotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TeacherRegisterActivity extends AppCompatActivity {
  private Button register;
    private Button alrsignin;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText contact;
    private EditText credential;
    private ProgressDialog progressDialog;
    private   String rname,remail,rpassword,rconfirmPassword,rcontact,rcredential;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private String branch;
    private Button nextWasteButton;
    Teacher teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_register_activity);
    register=findViewById(R.id.register_button);
    alrsignin= findViewById(R.id.already);
    nextWasteButton=new Button(TeacherRegisterActivity.this);
    teacher=new Teacher();
    reff=FirebaseDatabase.getInstance().getReference().child("Teacher");

    firebaseAuth=FirebaseAuth.getInstance();
    progressDialog=new ProgressDialog(this);
    name=findViewById(R.id.name);
    email=findViewById(R.id.email);
    password=findViewById(R.id.password);
    confirmPassword=findViewById(R.id.confirm_password);
    contact=findViewById(R.id.contact);
    credential=findViewById(R.id.credential);


    spinner=findViewById(R.id.spinner);
    //spinner.setBackgroundColor(Color.parseColor("#80d9b2"));
        String homes[]={"CS","EC","EE","ME","CE","CM"};
        final ArrayList<String> l=new ArrayList<String>();
        for(int i=0;i<homes.length;i++)
        {
            l.add(homes[i]);
        }
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,l);


        spinner.setAdapter(arrayAdapter);

      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              Object obj=adapterView.getItemAtPosition(i);
              branch=obj.toString();
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });

        // Toast.makeText(TeacherRegisterActivity.this, "Your Entry : "+rname+","+remail+","+rpassword+","+rcontact+","+rcredential, Toast.LENGTH_LONG).show();
     /*   Toast.makeText(TeacherLoginActivity.this, "SIGN UP BUTTON CLICKED", Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/

      //  Toast.makeText(TeacherRegisterActivity.this, "REGISTER ACT", Toast.LENGTH_LONG).show();
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(TeacherRegisterActivity.this, "REGISTER ACT"+branch, Toast.LENGTH_LONG).show();

                rname=name.getText().toString();
                remail=email.getText().toString();
                rpassword=password.getText().toString();
                rconfirmPassword=confirmPassword.getText().toString();
                rcontact=contact.getText().toString();
                rcredential=credential.getText().toString();

                if(TextUtils.isEmpty(remail))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
                    return;
                }if(TextUtils.isEmpty(rname))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Please enter name", Toast.LENGTH_LONG).show();
                    return;
                }if(TextUtils.isEmpty(rpassword))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(rcontact))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Please enter contact", Toast.LENGTH_LONG).show();
                    return;
                }if(TextUtils.isEmpty(rcredential))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Please enter your credential ID", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!rpassword.equalsIgnoreCase(rconfirmPassword))
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
                    return;
                }
                if(rpassword.length()<6)
                {
                    Toast.makeText(TeacherRegisterActivity.this, "Length of password should be more than 6", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.setMessage("Registering you....");
                teacher.setName(rname);
                teacher.setEmail(remail);
                teacher.setPassword(rpassword);
                teacher.setContact(rcontact);
                teacher.setCredential(rcredential);
                teacher.setBranch(branch);

                progressDialog.show();

                reff.child(rname).setValue(teacher);
                    // reff.child(remail).setValue(teacher); not working

                Runnable progressRunnable=new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.cancel();


                        nextWasteButton.performClick();
                        }
                };
                Handler pdHandller=new Handler();
                pdHandller.postDelayed(progressRunnable,3000);

                Toast.makeText(TeacherRegisterActivity.this, "You have successfully registered ", Toast.LENGTH_LONG).show();

                firebaseAuth.createUserWithEmailAndPassword(remail,rpassword)
                  .addOnCompleteListener(TeacherRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            try {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(TeacherRegisterActivity.this, new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                // Re-enable button

                                                if (task.isSuccessful()) {



                                                   Toast.makeText(TeacherRegisterActivity.this,"Link Sent",Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(TeacherRegisterActivity.this,
                                                            "Failed to send verification email.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });



                            }catch (Exception e)
                            {

                            }
                                                       //  Toast.makeText(TeacherRegisterActivity.this, "You have successfully registered : "+rname+","+remail+","+rpassword+","+rcontact+","+rcredential+","+branch, Toast.LENGTH_LONG).show();
                        }else
                        {
                            Toast.makeText(TeacherRegisterActivity.this, "Sign up failed ", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
       alrsignin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i=new Intent(TeacherRegisterActivity.this,TeacherLoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        nextWasteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

           Intent i=new Intent(TeacherRegisterActivity.this,TeacherDashboardActivity.class);
              i.putExtra("name",rname);
              i.putExtra("email",remail);
              i.putExtra("contact",rcontact);
               startActivity(i);
               finish();

            }
        });


    }
 }
