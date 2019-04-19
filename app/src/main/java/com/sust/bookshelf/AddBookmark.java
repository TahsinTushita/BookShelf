package com.sust.bookshelf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBookmark extends AppCompatActivity {
    Toolbar toolbar;
    Button button;
    EditText editText;
    public static final String EXTRA_BOOK = "bookObject";
    Book book;
    DatabaseReference reference;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bookmark);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button=findViewById(R.id.bookmarkBtn);
        editText=findViewById(R.id.bookmarkTextid);

        book= (Book) getIntent().getExtras().getSerializable(EXTRA_BOOK);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                string=editText.getText().toString().trim();
                reference= FirebaseDatabase.getInstance().getReference("Profile").child(LoginActivity.user);
                reference.child("bookmarks").child(book.getParent()).child("parent").setValue(book.getParent());
                reference.child("bookmarks").child(book.getParent()).child("title").setValue(book.getTitle());
                reference.child("bookmarks").child(book.getParent()).child("imgurl").setValue(book.getImgurl());
                reference.child("bookmarks").child(book.getParent()).child("author").setValue(book.getAuthor());
                reference.child("bookmarks").child(book.getParent()).child("pageno").setValue(string);
                Toast.makeText(AddBookmark.this,"Bookmark added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBookmark.this,HomePageActivity.class));
            }
        });
    }
}
