package com.example.autoplac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    EditText model,year,phone,description,image;

    Button btnAdd,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        model=(EditText) findViewById(R.id.txtModel);
        year=(EditText) findViewById(R.id.txtYear);
        phone=(EditText) findViewById(R.id.txtPhone);
        description=(EditText) findViewById(R.id.txtDescription);
        image=(EditText) findViewById(R.id.txtImage);

        btnAdd=(Button) findViewById(R.id.btnAdd);
        btnBack=(Button) findViewById(R.id.btnBack);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                clearAll();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void clearAll() {
        model.setText("");
        year.setText("");
        phone.setText("");
        description.setText("");
        image.setText("");


    }

    private void insertData() {
        Map<String,Object> map= new HashMap<>();
        map.put("model", model.getText().toString());
        map.put("year", year.getText().toString());
        map.put("phone", phone.getText().toString());
        map.put("description", description.getText().toString());
        map.put("image", image.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("vehicles").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this, "Data inserted Successfully.", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddActivity.this, "Error while insertion.", Toast.LENGTH_SHORT).show();
                    }
                });


    }


}