package com.sust.bookshelf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BorrowedBooks extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView noRequest;

    private RecyclerView recyclerView;
    private BorrowedBooksAdapter adapter;
    private FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();
    BorrowedBooksAdapter.OnItemClickListener listener;

    private ArrayList<Request> borrowedBooksList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        noRequest = findViewById(R.id.noRequestid);

        recyclerView = findViewById(R.id.borrowedbooks_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BorrowedBooksAdapter(this,borrowedBooksList, databaseReference,listener);
        recyclerView.setAdapter(adapter);
        fetchBooks();
    }

    private void fetchBooks() {
        databaseReference.getReference("Profile/"+LoginActivity.user+"/borrowedBooks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Request request = dataSnapshot.getValue(Request.class);
                System.out.println("BOokTitle " + request.getBookTitle());
                borrowedBooksList.add(request);
                adapter.notifyDataSetChanged();
                if(borrowedBooksList.size() != 0) noRequest.setVisibility(View.GONE);
                else noRequest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(borrowedBooksList.size() != 0) noRequest.setVisibility(View.GONE);
                else noRequest.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
