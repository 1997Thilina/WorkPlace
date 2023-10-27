package com.example.workplacE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.data.DataBufferRef;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MakeJobOffer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerOffer;
    private EditText job,company,location;
    private Button createJobButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_job_offer);

        job = findViewById(R.id.jobTitle);
        company = findViewById(R.id.companyName);
        location = findViewById(R.id.location);
        createJobButton = findViewById(R.id.postButton);

        loader = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        spinnerOffer = findViewById(R.id.spinnerOffer);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.jobCategory, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOffer.setAdapter(adapter);

        createJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jobName = job.getText().toString();
                String companyName = company.getText().toString();
                String locationName = location.getText().toString();
                String choice = spinnerOffer.getSelectedItem().toString();


                if(choice.equals("Select the job category")){
                    Toast.makeText(MakeJobOffer.this, "select the job category", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    loader.setMessage("posting");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    String currentUserId =mAuth.getCurrentUser().getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("users").child(currentUserId);
                    HashMap userInfo = new HashMap();
                    userInfo.put("job",jobName);
                    userInfo.put("company",companyName);
                    userInfo.put("location",locationName);
                    userInfo.put("category",choice);
                    userInfo.put("type","jobMaker");
                    userInfo.put("search","jobMaker"+ choice);

                    databaseReference.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MakeJobOffer.this, "offer was created successfully", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MakeJobOffer.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }

                            finish();
                            loader.dismiss();
                        }
                    });


                }

            }
        });


        //spinnerOffer.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //String choice = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}