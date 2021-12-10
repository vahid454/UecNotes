package com.uec.notes.uecnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class TeacherDashboardActivity extends AppCompatActivity {
private String name;
private String email,contact;
private LinearLayout teacherDashboard;
private FirebaseAuth firebaseAuth;

/*private EditText getopt;
private Button getOtpButton;
private Button verifyButton;

private String verificationCode;*/
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        builder = new AlertDialog.Builder(this);
        teacherDashboard=findViewById(R.id.teacher_dashboard);
        Intent i = getIntent();
        name = i.getStringExtra("name");
        email=i.getStringExtra("email");

      /*  contact=i.getStringExtra("contact");
        getopt=findViewById(R.id.getotp);
        getOtpButton=findViewById(R.id.getotpbutton);
        verifyButton=findViewById(R.id.verifybutton);
        */











        TextView t=new TextView(TeacherDashboardActivity.this);
        t.setText("Welcome "+name);
        t.setTextSize(28);
        t.setTextColor(Color.parseColor("#dbb714"));
        TextView greet=new TextView(TeacherDashboardActivity.this);
        greet.setText("Congratulations! You have successfully made an teacher profile.");
        greet.setTextSize(20);

        TextView ch=new TextView(TeacherDashboardActivity.this);
        ch.setText("One verification link has been sent to your email address, click that link and get verified.");
        ch.setTextSize(16);

        Button home=new Button(TeacherDashboardActivity.this);
        home.setText("Close & Verify");
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        FirebaseAuth.getInstance().signOut();

        teacherDashboard.addView(t);
        teacherDashboard.addView(greet);
        teacherDashboard.addView(new TextView(TeacherDashboardActivity.this));
        teacherDashboard.addView(ch);
        teacherDashboard.addView(new TextView(TeacherDashboardActivity.this));

        teacherDashboard.addView(home);
        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                new AlertDialog.Builder(TeacherDashboardActivity.this).setTitle("Exit Alert!!").setMessage("Do you really want to exit?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TeacherDashboardActivity.this,"Closing",Toast.LENGTH_LONG).show();
                       finish();
                       moveTaskToBack(false);

                        System.exit(0);

                    }
                }).setNegativeButton(android.R.string.no,null).show();
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
