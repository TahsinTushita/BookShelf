package com.sust.bookshelf;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    //Login box related things
    private EditText userName;
    private EditText passWord;
    private Button loginBtn,regBtn;

    static String user = "Anonymous";
    static DataSnapshot profileDataSnapshot;

    // Firebase related things
    private FirebaseDatabase database;
    private String username;
    private FirebaseAuth firebaseAuth;


    //Toolbar related things
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        //Find and set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //Find loginBox ids and match them
        userName = findViewById(R.id.userNameID);
        passWord = findViewById(R.id.passWordID);
        loginBtn = findViewById(R.id.btnLogin);
        regBtn = findViewById(R.id.btnReg);


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = userName.getText().toString().trim();
                    String pwd = passWord.getText().toString();

                    //01 This part is to hide softkeyboard
                    View nview = getCurrentFocus();
                    if (nview != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
                    }
                    //01 part ends here

                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
                        Snackbar.make(findViewById(android.R.id.content), R.string.invalid_login_msg, Snackbar.LENGTH_SHORT)
                                .show();
                    } else {
                        firebaseAuth.signInWithEmailAndPassword(username + "@sust.com", pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = firebaseAuth.getCurrentUser().getEmail();
                                    user = user.substring(0,user.lastIndexOf('@')).trim();
                                    Snackbar.make(findViewById(android.R.id.content), R.string.successful_login, Snackbar.LENGTH_SHORT).show();
                                    addUserBookList(username);
//                                    startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                                } else {
                                    Snackbar.make(findViewById(android.R.id.content), R.string.unsuccessful_login, Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            user = firebaseAuth.getCurrentUser().getEmail();
            user = user.substring(0,user.lastIndexOf('@')).trim();
            addUserBookList(user);
        }
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

            }
        });
    }

    private void addUserBookList(String username) {
        username = username.toLowerCase().trim();
        DatabaseReference query = FirebaseDatabase.getInstance().getReference("Profile/"+username+"/booklist");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileDataSnapshot = dataSnapshot;
                startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
//                Toast.makeText(LoginActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
