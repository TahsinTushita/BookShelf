package com.sust.bookshelf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PublicBooklistActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<ExistingBooklist> arrayList;
    ExistingBooklistAdapter adapter;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_booklist);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Public Booklist");

        recyclerView = findViewById(R.id.public_booklist_recyclerview);
        arrayList = new ArrayList<>();
        adapter = new ExistingBooklistAdapter(PublicBooklistActivity.this, arrayList,listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference("Profile/"+LoginActivity.user+"/publicbooklist");
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    ExistingBooklist existingBooklist = dataSnapshot.getValue(ExistingBooklist.class);
                    arrayList.add(existingBooklist);
                    Toast.makeText(PublicBooklistActivity.this,dataSnapshot.toString(),Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ExistingBooklistAdapter.OnItemClickListener listener = new ExistingBooklistAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ExistingBooklist existingBooklist) {
            startActivity(new Intent(PublicBooklistActivity.this,InsideExistingBooklistActivity.class).putExtra("listname",existingBooklist));
        }
    };

}
