package com.sust.bookshelf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddToExistingBooklist extends AppCompatActivity {

    private Book book;
    private Spinner spinner;
    private List<String> entries;
    private Button addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_existing_booklist);
        book = (Book) getIntent().getExtras().get("bookObject");
        spinner = findViewById(R.id.existing_bookslist_spinner);
        entries = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                entries
        );
        addBtn = findViewById(R.id.addBtn);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile/"+LoginActivity.user+"/publicbooklist");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String listname = (String) dataSnapshot.child("listname").getValue();
                entries.add(listname);
                spinner.setAdapter(adapter);
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
        addBtn.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String listname = spinner.getSelectedItem().toString();
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Profile/"+LoginActivity.user+"/publicbooklist/"+listname);
            dref.child(book.getParent()).child("parent").setValue(book.getParent());
        }
    };
}
