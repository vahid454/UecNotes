package com.uec.notes.uecnotes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TeacherLoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private LinearLayout parent;
    private Button login;
    private EditText email, password;
    private String email_text, password_text;
    private Button sign_up_button;

    private Button forgotButton;
    private DatabaseReference reff;

    private GoogleApiClient mGoogleApiclient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
     FirebaseUser user=firebaseAuth.getCurrentUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_login_activity);
        parent = findViewById(R.id.teacher_login);
        login = findViewById(R.id.sign_in_button);
        sign_up_button = findViewById(R.id.sign_up_button);
        email = findViewById(R.id.lemail);
        password = findViewById(R.id.lpassword);
        forgotButton = findViewById(R.id.forgot_button);
        progressDialog=new ProgressDialog(TeacherLoginActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        reff=FirebaseDatabase.getInstance().getReference().child("Teacher");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleApiclient = new GoogleApiClient.Builder(this).enableAutoManage(TeacherLoginActivity.this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        //Toast.makeText(getApplicationContext(),"IN LOGIN ACT",Toast.LENGTH_SHORT).show();
        forgotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ShowRecoverPasswordDialog();

                //Toast.makeText(getApplicationContext(), "IN FORGOT ACT", Toast.LENGTH_SHORT).show();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(),"IN LOGIN ACT",Toast.LENGTH_LONG).show();

              //  signIn();
                CustomLogin();
                /*
               email_text=email.getText().toString();
                password_text=password.getText().toString();
                Toast.makeText(TeacherLoginActivity.this,"SUCCESSFULLY LOGGED IN"+email_text+"&"+password_text, Toast.LENGTH_LONG).show();

                reff=FirebaseDatabase.getInstance().getReference().child("Teacher").child("Vahid");

                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Toast.makeText(TeacherLoginActivity.this,"SUCCESSFULLY LOGGED IN", Toast.LENGTH_LONG).show();

                        String demail= (String) dataSnapshot.child("email").getValue().toString();
                        String dname=(String) dataSnapshot.child("name").getValue().toString();
                        String dcredential=(String) dataSnapshot.child("credential").getValue().toString();
                        Toast.makeText(TeacherLoginActivity.this,"SUCCESSFULLY LOGGED IN", Toast.LENGTH_LONG).show();

                        Intent i=new Intent(TeacherLoginActivity.this,TeacherDashboardActivity.class);
                        i.putExtra("name",dname.toString());
                        startActivity(i);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(TeacherLoginActivity.this,"WRONG PASSWORD OR EMAIL", Toast.LENGTH_LONG).show();


                  }
                });

        // This is for saving data to heroku
                RequestQueue requestQueue = Volley.newRequestQueue(TeacherLoginActivity.this);

                String url = "https://uecnotesserver.herokuapp.com/uecnotes/login?email="+email_text+"&password="+password_text+"";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(TeacherLoginActivity.this,""+response, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "ERRORRRRRR", Toast.LENGTH_LONG).show();

                    }
                });
                requestQueue.add(stringRequest);
         */


            }

        });


      /*  if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), TeacherDashboardActivity.class));
        }
**/
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(TeacherLoginActivity.this, TeacherRegisterActivity.class);
                startActivity(i);
                finish();

            }
        });


    }

    private void ShowRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText editEmail=new EditText(this);
        editEmail.setHint("Enter recovery email");
        editEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
      editEmail.setX(30);
        editEmail.setMinEms(16);

        linearLayout.addView(editEmail);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        //buttons
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email=editEmail.getText().toString().trim();
                if(email.length()==0)
                {
                   // Toast.makeText(getApplicationContext(),"Please enter email",Toast.LENGTH_SHORT).show();
                   // editEmail.setHighlightColor(Color.parseColor("#80d9b2"));

                    ShowRecoverPasswordDialog();

                }else{
                    BeginRecovery(email);
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss dialog
                dialogInterface.dismiss();
            }
        });

    builder.create().show();
    }

    private void BeginRecovery(String email) {
        progressDialog.setMessage("Recovery link sending...");
        progressDialog.show();


        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful())
                    {

                        Toast.makeText(getApplicationContext(),"Recovery link sent",Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Failed..",Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void CustomLogin()

    {
        email_text=email.getText().toString().trim();
        password_text=password.getText().toString().trim();
        if(TextUtils.isEmpty(email_text))
        {
            Toast.makeText(TeacherLoginActivity.this, "Please enter email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password_text))
        {
            Toast.makeText(TeacherLoginActivity.this, "Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Signing In....");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email_text,password_text)
            .addOnCompleteListener(TeacherLoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                       final Intent i=new Intent(TeacherLoginActivity.this,TeacherMainDashboard.class);

                      //  i.putExtra("name",rname);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null && user.isEmailVerified()) {

                           final String email = user.getEmail();


                            i.putExtra("email",email);


                            reff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot ds:dataSnapshot.getChildren())
                                    {
                                        Teacher teacher=ds.getValue(Teacher.class);
                                        if(email.equalsIgnoreCase(teacher.getEmail()))
                                        {
                                        final String    nameOfActiveUser=teacher.getName();
                                         i.putExtra("name",nameOfActiveUser);
                                         i.putExtra("branch",teacher.getBranch());

                                        }

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });



                            Runnable progressRunnable=new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.cancel();

                                    startActivity(i);


                                }
                            };
                            Handler pdHandller=new Handler();
                            pdHandller.postDelayed(progressRunnable,3000);


                        }
 // startActivity(new Intent(getApplicationContext(),TeacherDashboardActivity.class));
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(TeacherLoginActivity.this, "Login failed or user not available", Toast.LENGTH_LONG).show();
                    }
                }
            });

    }

    private void signIn(){
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiclient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
@Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
{
super.onActivityResult(requestCode,resultCode,data);
if(requestCode==RC_SIGN_IN){
    GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);

if(result.isSuccess()){
    GoogleSignInAccount account=result.getSignInAccount();
    Toast.makeText(getApplicationContext(),"Success"+account.getDisplayName(),Toast.LENGTH_LONG).show();
    authWithGoogle(account);
}
}

}
private void authWithGoogle(GoogleSignInAccount account)
{
    AuthCredential credential=GoogleAuthProvider.getCredential(account.getIdToken(),null);
    firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
               startActivity(new Intent(getApplicationContext(),TeacherDashboardActivity.class));
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_LONG).show();
            }
        }
    });
}

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
