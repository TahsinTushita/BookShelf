package com.sust.bookshelf;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookOwners extends AppCompatActivity {

    private RecyclerView bookOwnersRecyclerView;
    private ArrayList<ProfileInfo> profileInfos;
    private BookOwnerAdapter adapter;
    private Book book;

    BookOwnerAdapter.OnClickListener listener = new BookOwnerAdapter.OnClickListener() {
        @Override
        public void onClick() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_owners);
        book = (Book) getIntent().getExtras().get("bookObject");

        bookOwnersRecyclerView = findViewById(R.id.bookOwnerRecyclerView);
        bookOwnersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileInfos = new ArrayList<>();
        adapter = new BookOwnerAdapter(this,profileInfos,listener);

        DatabaseReference dref = FirebaseDatabase.getInstance().getReference("Books/"+book.getParent()+"/users");
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ProfileInfo profileInfo = dataSnapshot.getValue(ProfileInfo.class);
                profileInfos.add(profileInfo);
                bookOwnersRecyclerView.setAdapter(adapter);
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
}
