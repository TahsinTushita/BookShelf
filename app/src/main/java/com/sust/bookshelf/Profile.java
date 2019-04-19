package com.sust.bookshelf;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    private String user;
    private String EXTRA_PROFILE_ID = "profileID";

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    private TextView drawerUserName,username,email,address;
    private Button editBtn;
    private ProfileInfo profileInfo = new ProfileInfo();
    private DatabaseReference profileInfodatabase,profileDatabase;
    Button requestBtn;

    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        drawer = (DrawerLayout) findViewById(R.id.drawerid);
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_id);
        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.nav_open,R.string.nav_close);
        drawerUserName = navigationView.getHeaderView(0).findViewById(R.id.navuserid);
        String user = LoginActivity.user.toUpperCase();
        drawerUserName.setText(user);

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        username = findViewById(R.id.profileUsername);
        email = findViewById(R.id.profileEmail);
        address = findViewById(R.id.profileAddress);
        requestBtn = findViewById(R.id.requestBtnid);

        profileInfodatabase = FirebaseDatabase.getInstance().getReference("Profile");
        profileDatabase = FirebaseDatabase.getInstance().getReference("Profile");

        profilePhoto = findViewById(R.id.profilePhoto);

        String map = (String) getIntent().getExtras().get("from");
        if (map != null)
        if (map.equals("MapActivity")){
            requestBtn.setVisibility(View.VISIBLE);
        }

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = profileInfodatabase.child(getIntent().getStringExtra(EXTRA_PROFILE_ID)).child("booklist")
                        .child(BookProfile.currentBook.getParent()).child("requests").child(LoginActivity.user);
                db.child("username").setValue(LoginActivity.user);
                db.child("parent").setValue(BookProfile.currentBook.getParent());
                db.child("bookTitle").setValue(BookProfile.currentBook.getTitle());

                db = profileDatabase.child(LoginActivity.user).child("requestedBooks")
                                    .child(BookProfile.currentBook.getParent());
                db.child("username").setValue(getIntent().getStringExtra(EXTRA_PROFILE_ID));
                db.child("bookTitle").setValue(BookProfile.currentBook.getTitle());
                db.child("parent").setValue(BookProfile.currentBook.getParent());
                Toast.makeText(Profile.this,"Request sent",Toast.LENGTH_LONG).show();
            }
        });


        Query mQuery = FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("username").equalTo(getIntent().getStringExtra(EXTRA_PROFILE_ID));
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    ProfileInfo info = postSnapshot.getValue(ProfileInfo.class);
                    profileInfo = info;
                    username.setText(profileInfo.getName());
                    address.setText(profileInfo.getAddress());
                    email.setText(profileInfo.getEmail());
                    Picasso.get().load(profileInfo.getProfilephoto()).into(profilePhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        user = LoginActivity.user;

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerid);

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }

        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();
        Intent intent = null;

        switch(id) {

            case R.id.profileid:
                intent = new Intent(this, Profile.class);
                intent.putExtra("profileID", LoginActivity.user);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.bookListid:
                intent = new Intent(this, BookList.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.wishListid:
                intent = new Intent(this, WishList.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.homePage:
                intent = new Intent(this, HomePageActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.updProfile:
                intent = new Intent(this,UpdateProfile.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.requestsid:
                intent = new Intent(this,RequestsActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.requestedBooksid:
                intent = new Intent(this,RequestedBooksActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.borrowedBooksid:
                intent = new Intent(this,BorrowedBooks.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.lentBooksid:
                intent = new Intent(this,LentBooks.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.allBooks:
                intent = new Intent(this,AllBooksActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.requestForBook:
                intent = new Intent(this,RequestForBookActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.help_and_support:
                intent = new Intent(this,UserManual.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_public_booklist:
                intent = new Intent(this, PublicBooklistActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
        startActivity(intent);
        return true;
    }

}
