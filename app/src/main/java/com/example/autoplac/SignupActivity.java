package com.example.autoplac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth;
EditText edtname,edtemail,edtpass;
Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button backtologin = findViewById(R.id.backtologin);

        Button signup = findViewById(R.id.btnsignup);
       edtname = findViewById(R.id.edtName);
       edtemail = findViewById(R.id.edtEmail);
        edtpass = findViewById(R.id.edtPass);

      spinner = findViewById(R.id.spinner1);
        List<String> Categories = new ArrayList<>();
        Categories.add("Korisnik");
        Categories.add("Admin");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        signup.setOnClickListener(v -> {
            if (edtname.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Molimo Vas unesite Vase ime", Toast.LENGTH_SHORT).show();
            }

            if (edtemail.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Molimo Vas unesite Vasu email adresu", Toast.LENGTH_SHORT).show();
            }

            if (edtpass.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Molimo Vas unesite Vasu lozinku", Toast.LENGTH_SHORT).show();
            }

            firebaseAuth = FirebaseAuth.getInstance();
            if (!(edtname.getText().toString().isEmpty() && edtemail.getText().toString().isEmpty() && edtpass.getText().toString().isEmpty())) {
                firebaseAuth.createUserWithEmailAndPassword(edtemail.getText().toString(), edtpass.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addUser();
                        Intent in = new Intent(SignupActivity.this, MainActivity.class);

                        startActivity(in);
                    } else {

                        Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    backtologin.setOnClickListener(v -> {
        Intent in=new Intent(SignupActivity.this,Login.class);
        startActivity(in);
    });




    }

    private void addUser(){
        Map<String,Object> map= new HashMap<>();
        map.put("name",edtname.getText().toString());
        map.put("email", edtemail.getText().toString());
      map.put("isAdmin",spinner.getSelectedItemPosition());
       FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();

        String emails = firebaseAuth1.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(emails)
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignupActivity.this, "Data inserted Successfully.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Error while insertion.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}