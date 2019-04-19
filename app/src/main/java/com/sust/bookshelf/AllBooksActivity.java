package com.sust.bookshelf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity {

    SearchresultsAdapter.OnItemClickListener listener = new SearchresultsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Book book) {
            Intent i = new Intent(AllBooksActivity.this,BookProfile.class);
            i.putExtra("bookObject",book);
            startActivity(i);
        }
    };

    private Toolbar toolbar;
    private ArrayList<Book> booklist = new ArrayList<>();
    private RecyclerView recyclerView;
    private Spinner filterSpinner;
    private Button selectBtn;
    private String spinnerItem;
    private DatabaseReference databaseReference;
    private SearchresultsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);

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

         adapter = new SearchresultsAdapter(this,booklist,listener);

        recyclerView = findViewById(R.id.all_books_recyclerView);
        filterSpinner = findViewById(R.id.allbooksfilter_spinner);
        selectBtn = findViewById(R.id.spinnerselectbtn);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    booklist.add(book);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Book> bookArrayList = new ArrayList<>();
                spinnerItem = filterSpinner.getSelectedItem().toString();
                databaseReference = FirebaseDatabase.getInstance().getReference("Books");
                Query query = databaseReference.orderByChild("category").equalTo(spinnerItem);
                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Book book = dataSnapshot.getValue(Book.class);
                        bookArrayList.add(book);
                        adapter = new SearchresultsAdapter(AllBooksActivity.this,bookArrayList,listener);
                        recyclerView.setAdapter(adapter);
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
        });
    }
}
