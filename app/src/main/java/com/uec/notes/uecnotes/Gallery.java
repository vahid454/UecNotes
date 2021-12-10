package com.uec.notes.uecnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {
private ListView galleryList;
private MyAdapter myAdapter;
private ArrayList<String> data=new ArrayList<>();
private ArrayList<String> image=new ArrayList<>();
    private List<UploadImage> uploadImages=new ArrayList<UploadImage>();
    private DatabaseReference reff;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private ConstraintLayout parent;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        galleryList=findViewById(R.id.gallery_list);
        myAdapter=new MyAdapter();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        parent=findViewById(R.id.gallery_layout);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        reff=FirebaseDatabase.getInstance().getReference("images");

        reff.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                    UploadImage uploadImage=postSnapshot.getValue(UploadImage.class);
                   uploadImages.add(uploadImage);

                }
                System.out.println("KUTTA");
                /*
                for(int i=0;i<uploadImages.size();i++)
                {
                    image.add(uploadImages.get(i).getUrl());
                    data.add(uploadImages.get(i).getName());
                    System.out.println("AWESOME"+image.get(i));
                   System.out.println("AWESOME DATA"+data.get(i));
                }*/

                if(uploadImages.size()==0)
                {
                    TextView t=new TextView(Gallery.this);
                    t.setTextSize(24);
                    t.setTextColor(Color.parseColor("#80d9b2"));
                    t.setText(R.string.no_files);
                    t.setPadding(260,5,10,10);
                    t.setGravity(1);
                    parent.addView(t);
                }
                galleryList.setAdapter(myAdapter);
                progressDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final ProgressDialog pd=new ProgressDialog(Gallery.this);
        pd.setMessage("Deleting....");
        galleryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                UploadImage uploadImage=uploadImages.get(i);
               // System.out.println("FILE NAME AMAZE"+uploadImage.getName());

            final String filename = (String)uploadImage.getName().toString();
                if (user != null) {


                    new AlertDialog.Builder(Gallery.this).setTitle("Delete " + filename + " ?").setMessage("Do you really want to delete this file?").setIcon(android.R.drawable.ic_dialog_dialer).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pd.show();
                            reff = FirebaseDatabase.getInstance().getReference("images").child(filename.trim());

                            StorageReference sr = FirebaseStorage.getInstance().getReference("images" ).child( filename.trim() + ".jpg");
                            sr.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            reff.removeValue()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            Toast.makeText(Gallery.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();

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
                                    Toast.makeText(Gallery.this, "File cannot be deleted.", Toast.LENGTH_LONG).show();

                                }
                            });


                        }
                    }).setNegativeButton(android.R.string.no, null).show();

                }else
                {
                    Toast.makeText(Gallery.this,"You are not a teacher",Toast.LENGTH_LONG).show();
                    return true;
                }
                return true;

            }
        });

        galleryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                UploadImage uploadImage=uploadImages.get(i);
                Intent intent=new Intent();
                // intent.setType(Intent.ACTION_VIEW);
                intent.setAction(Intent.ACTION_QUICK_VIEW);
                intent.setData(Uri.parse(uploadImage.getUrl()));
                startActivity(intent);
            }
        });






    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return uploadImages.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           //crerat view
            View v=LayoutInflater.from(getApplicationContext()).inflate(R.layout.itemlayout,null);
           ImageView imageView=v.findViewById(R.id.image_gallery);
            TextView textView=v.findViewById(R.id.gallery_text);
           // imageView.setImageResource(image.get(i));

           // imageView.setImageResource(image.get(i));

            String url=uploadImages.get(i).getUrl();

            Picasso.get().load(url).into(imageView);
           // imageView.setImageURI(Uri.parse(url));
            textView.setGravity(1);
            textView.setText(uploadImages.get(i).getName());
            System.out.println("DATA GOING FROM HERE");

            return v;
        }
    }
}
