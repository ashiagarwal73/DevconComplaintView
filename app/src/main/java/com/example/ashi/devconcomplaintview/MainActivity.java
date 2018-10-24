package com.example.ashi.devconcomplaintview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=database.getReference().child("Complaint");

        recyclerView=findViewById(R.id.view_complain_recycler_view);
        progressBar=findViewById(R.id.view_complain_progressBar);
        final List<Complaint> complaints=new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                complaints.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Complaint complaint=snapshot.getValue(Complaint.class);
                    complaints.add(complaint);
                }
                Collections.reverse(complaints);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(linearLayoutManager);
                ViewComplainAdapter viewComplainAdapter=new ViewComplainAdapter(complaints);
                recyclerView.setAdapter(viewComplainAdapter);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
