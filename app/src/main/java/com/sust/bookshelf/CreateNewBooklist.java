package com.sust.bookshelf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.sust.bookshelf.BookProfile.EXTRA_BOOK;

public class CreateNewBooklist extends AppCompatActivity {
    Toolbar toolbar;
    EditText booklistName;
    Button createBtn;
    String listName,userName;
    DatabaseReference reference;
    private Book book;
    public static Book currentBook;
    public static final String EXTRA_BOOK = "bookObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_booklist);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create a new Public Booklist");

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
            userName = firebaseUser.getEmail();
        userName = userName.substring(0,userName.lastIndexOf('@'));

        book = (Book) getIntent().getExtras().getSerializable(EXTRA_BOOK);
        currentBook = book;

        booklistName = findViewById(R.id.newBooklistNameText);
        createBtn = findViewById(R.id.createNewBooklistBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listName = booklistName.getText().toString().trim();
                reference = FirebaseDatabase.getInstance().getReference("Profile").child(userName).child("publicbooklist");
                reference.child(listName).child(book.getParent()).child("parent").setValue(book.getParent());
                reference.child(listName).child("listname").setValue(listName);
                Toast.makeText(CreateNewBooklist.this,"Added to your new public booklist "+listName,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateNewBooklist.this,HomePageActivity.class);
                startActivity(intent);
            }
        });
    }
}
