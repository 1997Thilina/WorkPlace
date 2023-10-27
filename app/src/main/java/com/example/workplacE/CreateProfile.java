package com.example.workplacE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

public class CreateProfile extends AppCompatActivity {

    private Spinner spinnerProfile;
    private EditText employment,name;
    private TextView addPdf;
    private Button createProfileButton;

    private ProgressDialog loader;

    private Uri resultUri;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        employment = findViewById(R.id.employment);
        name = findViewById(R.id.name);
        addPdf = findViewById(R.id.addPdf);
        createProfileButton = findViewById(R.id.createButton);

        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        spinnerProfile = findViewById(R.id.spinnerJob);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProfile.setAdapter(adapter);

        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });



        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employmentName = employment.getText().toString();
                String yourName = name.getText().toString();

                String choice = spinnerProfile.getSelectedItem().toString();


                if (choice.equals("Select the job category")) {
                    Toast.makeText(CreateProfile.this, "select the job category", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    loader.setMessage("Creating");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String currentUserId = mAuth.getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(currentUserId);
                    HashMap userInfo = new HashMap();
                    userInfo.put("userId", currentUserId);
                    userInfo.put("employment", employmentName);
                    userInfo.put("name", yourName);
                    userInfo.put("category", choice);
                    userInfo.put("type", "profileMaker");

                    userInfo.put("search", "profileMaker" + choice);

                    if(resultUri != null){

                        final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                .child("CvFiles").child(currentUserId);
                        filePath.putFile(resultUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while(!uriTask.isComplete());
                                        Uri url = uriTask.getResult();
                                        String urlPdf = url.toString();
                                        databaseReference = FirebaseDatabase.getInstance().getReference()
                                                .child("CVLinks").child(mAuth.getCurrentUser().getUid().toString());
                                        HashMap pdfInfo = new HashMap();
                                        pdfInfo.put("urlPdf",urlPdf);
                                        databaseReference.updateChildren(pdfInfo);

                                    }
                                });

                    }


                    databaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(CreateProfile.this, "Profile was created successfully", Toast.LENGTH_SHORT).show();
                                Intent n = new Intent(CreateProfile.this,MainActivity2.class);
                                startActivity(n);
                            } else {
                                Toast.makeText(CreateProfile.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });




                    //finish();
                    loader.dismiss();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data != null){
            resultUri = data.getData();
            String fileName =System.currentTimeMillis()+"";
            //String pdfName = data.getDataString();
            addPdf.setText(fileName);
        }
    }

}