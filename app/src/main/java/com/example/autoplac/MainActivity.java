package com.example.autoplac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Button btnOdjava;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    FloatingActionButton floatingActionButton;
    FirebaseDatabase databse = FirebaseDatabase.getInstance();
    DatabaseReference databaseUsers = databse.getReference("users");
    int isAdminss;
    String id = mAuth.getCurrentUser().getUid();
    DatabaseReference username = databaseUsers.child(id).child("isAdmin");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        floatingActionButton=(FloatingActionButton)findViewById(R.id.floatingActionButton4);
        btnOdjava = findViewById(R.id.odjava);

        //odjavi korisnika
        btnOdjava.setOnClickListener(v -> {
            FirebaseAuth fbOdjava = FirebaseAuth.getInstance();
            fbOdjava.signOut();
            finish();
            Toast.makeText(MainActivity.this, "Korisnik odjavljen", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,Login.class);
            startActivity(intent);
            finish();
        });

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("vehicles"),MainModel.class)
                .build();

        mainAdapter=new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);


///prikazi koji je korisnik
        username.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                isAdminss = snapshot.getValue(Integer.class);
                hideButtons();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

            mainAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.startListening();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item=menu.findItem(R.id.search);

        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                txtSearch(query);
                ///
                hideButtons();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {


                txtSearch(query);
///
                hideButtons();
return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("vehicles").orderByChild("model").startAt(str).endAt(str+"~"), MainModel.class)
                        .build();
        mainAdapter= new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }


    public void hideButtons(){

        if (isAdminss == 1){
            floatingActionButton.setVisibility(View.VISIBLE);
            mainAdapter.activateButtons(true);
        }else{
            floatingActionButton.setVisibility(View.INVISIBLE);
            mainAdapter.activateButtons(false);

        }




    }
}