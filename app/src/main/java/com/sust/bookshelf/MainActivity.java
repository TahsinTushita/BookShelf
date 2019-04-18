package com.sust.bookshelf;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sust.bookshelf.homepage.Homepage;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText,passwordEditText;
    private Button loginBtn,regBtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginBtn = findViewById(R.id.loginBtn);
        regBtn = findViewById(R.id.regBtn);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            View nview = getCurrentFocus();
            if (nview != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nview.getWindowToken(), 0);
            }

            final String username = usernameEditText.getText().toString().trim();
            String pwd = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
                Snackbar.make(findViewById(android.R.id.content), R.string.invalid_login_msg, Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(username + "@sust.com", pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user = username.trim();
                            Snackbar.make(findViewById(android.R.id.content), R.string.successful_login, Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, Homepage.class));
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.unsuccessful_login, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }
    };
}
