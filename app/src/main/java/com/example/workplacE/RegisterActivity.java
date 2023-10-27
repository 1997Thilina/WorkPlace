package com.example.workplacE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText Email,Password,fName,lName;
    Button Register;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        Email = findViewById(R.id.entEmail);
        Password = findViewById(R.id.entPassword);
        Register = findViewById(R.id.regbutton);

        firebaseAuth = FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String ffName = fName.getText().toString();
                String llName = lName.getText().toString();
                String password = Password.getText().toString();

                if(email.isEmpty() || password.isEmpty() || ffName.isEmpty() || llName.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Field empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(RegisterActivity.this,"Successfully registered",Toast.LENGTH_SHORT).show();
                                    Intent k = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(k);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this,""+e,Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

    }
}