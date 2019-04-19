package com.sust.bookshelf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BecomeSeller extends AppCompatActivity {
    Toolbar toolbar;
    EditText priceText;
    Button priceButton;
    Integer bookPrice;
    String price;
    Book book;
    public static final String EXTRA_BOOK = "bookObject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_seller);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        priceButton = findViewById(R.id.setPricebtn);
        priceText = findViewById(R.id.sellerPriceText);
        book = (Book) getIntent().getExtras().getSerializable("bookObject");

        priceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = priceText.getText().toString().trim();
                bookPrice = Integer.parseInt(price);
                DatabaseReference newBook = FirebaseDatabase.getInstance().getReference("Profile").
                        child(LoginActivity.user).child("booklist");
                newBook.child(book.getParent()).child("parent").setValue(book.getParent());
                newBook.child(book.getParent()).child("availability").setValue(4);
                newBook.child(book.getParent()).child("price").setValue(bookPrice);
                DatabaseReference newUser = FirebaseDatabase.getInstance().
                        getReference("Books");
                newUser.child(book.getParent()).child("users").child(LoginActivity.user).
                        child("availability").setValue(4);
                newUser.child(book.getParent()).child("users").child(LoginActivity.user).
                        child("username").setValue(LoginActivity.user);
                Toast.makeText(BecomeSeller.this,"Added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BecomeSeller.this,HomePageActivity.class));
            }
        });
    }

}
