package com.example.autoplac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {
FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin=findViewById(R.id.btnLogin);
        Button btn_signup=findViewById(R.id.signup);
        EditText login_edtemail=findViewById(R.id.login_edtemail);
        EditText login_edtpass=findViewById(R.id.login_edtpass);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login_edtemail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Molimo Vas unesite Vasu email adresu", Toast.LENGTH_SHORT).show();
                }
                if (login_edtpass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Molimo Vas unesite Vasu lozinku", Toast.LENGTH_SHORT).show();
                }
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (!(login_edtemail.getText().toString().isEmpty() && login_edtpass.getText().toString().isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(login_edtemail.getText().toString(), login_edtpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent in=new Intent(Login.this,MainActivity.class);
                                startActivity(in);
                                finish();
                        }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                                
                    });
                }
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in=new Intent(Login.this,SignupActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);

            }
        });


    }


}