package com.sust.bookshelf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class InsideExistingBooklistActivity extends AppCompatActivity {

    private ExistingBooklist existingBooklist;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView inside_booklist_recyclerview;
    private ArrayList<Book> books;
    private SearchresultsAdapter listAdapter;
    SearchresultsAdapter.OnItemClickListener listener = new SearchresultsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Book book) {
            Intent i = new Intent(InsideExistingBooklistActivity.this
                    ,BookProfile.class);
            i.putExtra("bookObject",book);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_existing_booklist);
        existingBooklist = (ExistingBooklist) getIntent().getExtras().get("listname");
        inside_booklist_recyclerview = findViewById(R.id.inside_public_booklist_recyclerview);
        inside_booklist_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        books = new ArrayList<>();
        listAdapter = new SearchresultsAdapter(this,books,listener);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Profile/"+LoginActivity.user+"/publicbooklist/"+existingBooklist.getListname());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.child("parent").getValue() != null) {
                    String book = dataSnapshot.child("parent").getValue().toString();
                    Query dref = FirebaseDatabase.getInstance().getReference("Books").orderByChild("parent").equalTo(book);
                    dref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Book nbook = dataSnapshot.getValue(Book.class);
                            nbook.setListRef(existingBooklist.getListname());
                            books.add(nbook);
                            inside_booklist_recyclerview.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
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
}
