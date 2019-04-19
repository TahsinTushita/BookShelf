package com.sust.bookshelf;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookProfile extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;

    private Book book;
    private ProfileInfo profileInfo;
    public static final String EXTRA_BOOK = "bookObject";

    private ProgressDialog mapWaitDialog;
    private ImageView bookCover;
    private TextView bookAuthor;
    private TextView bookTitle;
    private TextView bookISBN;
    private Button availability;
    private Button booklistbtn,wishlishbtn,publicBooklistbtn,sellerbtn;
    private TextView drawerUserName;
    private TextView popupReview;
    TextView noReviewText;
    private String userName;

    private RecyclerView reviewView;
    private BookReviewAdapter reviewAdapter;
    private ArrayList<BookReview> reviewArrayList;
    private ArrayList<ProfileInfo> profileInfoArrayList;
    private ArrayList<Users> usersArrayList = new ArrayList<Users>();
    private DatabaseReference reviewDatabase,profileDatabase;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    public static Book currentBook;

    private View rootview;

    private DatabaseReference bookDatabaseReference,profileDatabaseReference,userDatabaseReference,newUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_profile);
        rootview = findViewById(android.R.id.content);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        noReviewText = findViewById(R.id.noReviewText);
        mapWaitDialog = new ProgressDialog(this);

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

        book = (Book) getIntent().getExtras().getSerializable(EXTRA_BOOK);
        currentBook = book;

        bookCover = findViewById(R.id.bookCover);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        availability = findViewById(R.id.checkAvailability);
        booklistbtn = findViewById(R.id.bookListid);
        wishlishbtn = findViewById(R.id.wishListid);
        publicBooklistbtn = findViewById(R.id.addToPublicList);
        sellerbtn = findViewById(R.id.becomeSeller);

        availability.setOnClickListener(this);
        booklistbtn.setOnClickListener(this);
        wishlishbtn.setOnClickListener(this);
        publicBooklistbtn.setOnClickListener(this);
        sellerbtn.setOnClickListener(this);

        Picasso.get().load(book.getImgurl()).into(bookCover);

        bookAuthor.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());

        userName = "Anonymous";

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        userName = firebaseUser.getEmail();
        userName = userName.substring(0,userName.lastIndexOf('@'));

        reviewDatabase = FirebaseDatabase.getInstance().getReference("Books").child(book.getParent()).child("reviews");
        bookDatabaseReference = FirebaseDatabase.getInstance().getReference("Books").
                child(book.getParent()).child("users");
        profileDatabaseReference = FirebaseDatabase.getInstance().getReference("Profile")
                .child(userName).child("booklist");



        //String adrr = profileDatabase.getDatabase().toString();
        //Recent Reviews
        reviewView = findViewById(R.id.reviewView);
        reviewView.setLayoutManager(new LinearLayoutManager(this));
        reviewArrayList = new ArrayList<>();
        reviewAdapter = new BookReviewAdapter(BookProfile.this, reviewArrayList,listener);
        reviewView.setAdapter(reviewAdapter);


//        Recent Reviews fetching from firebase
        reviewDatabase.addValueEventListener(reviewValueEventListener);
        bookDatabaseReference.addValueEventListener(usersValueEventListener);
    }

    ValueEventListener reviewValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                reviewArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BookReview review = snapshot.getValue(BookReview.class);
                    reviewArrayList.add(review);
                }
                if(reviewArrayList.size()>0) noReviewText.setVisibility(View.GONE);
                else noReviewText.setVisibility(View.VISIBLE);
                BookReviewAdapter adapter = new BookReviewAdapter(BookProfile.this,reviewArrayList,listener);
                reviewView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else return;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener usersValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                usersArrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    if(user.getAvailability() == 1)
                    usersArrayList.add(user);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void startPosting(View view) {
        Intent intent = new Intent(this,PostReview.class);
        intent.putExtra("bookParent",book);
        startActivity(intent);
    }

    BookReviewAdapter.OnItemClickListener listener = new BookReviewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BookReview book) {
            LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_window, null);
            popupReview = popupView.findViewById(R.id.popupReview);            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupReview.setText(book.getPostDesc());
            popupWindow.setAnimationStyle(R.style.popup_window_animation);            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        }

        @Override
        public void onLongClick(final BookReview book) {
            LayoutInflater inflater = (LayoutInflater)
            getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = inflater.inflate(R.layout.popup_delete, null);
            popupReview = popupView.findViewById(R.id.btnRemove);
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true; // lets taps outside the popup also dismiss it
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            popupReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books/"+currentBook.getParent()+"/reviews/"+LoginActivity.user);
                    if (book.getUsername().equals(LoginActivity.user)) {
                        databaseReference.setValue(null);
                        Snackbar.make(rootview,"Post has been removed!Will be processed Soon",Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(rootview,"Not your post",Snackbar.LENGTH_LONG).show();
                    }
                    reviewAdapter.notifyDataSetChanged();
                }
            });
        }
    };
    Integer cnt = 0;


    public void addToBooklist(){
        DatabaseReference newBook;
//
//        newBook = profileDatabase.push();
//        newBook.setValue(book.getParent());

        newBook = FirebaseDatabase.getInstance().getReference().child("Profile").child(userName).child("booklist");
        newBook.child(book.getParent()).child("parent").setValue(book.getParent());
        newBook = FirebaseDatabase.getInstance().getReference("TopBooks/"+currentBook.getParent());
        newBook.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                cnt = dataSnapshot.getValue(Integer.class);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                reviewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        newBook.child("count").setValue(cnt+1);
        }

    public void addToWishList(){
        DatabaseReference newBook;
        newBook = FirebaseDatabase.getInstance().getReference().child("Profile").child(userName).child("wishlist");
        newBook.child(book.getParent()).child("parent").setValue(book.getParent());
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id==R.id.bookListid) {
            addToBooklist();
            AlertDialog alertDialog = new AlertDialog.Builder(BookProfile.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Do you want to make this book available to borrow?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    newUser = bookDatabaseReference.child(userName);
                    newUser.child("username").setValue(userName);
                    newUser.child("availability").setValue(0);
                    profileDatabaseReference.child(book.getParent()).child("availability").setValue(0);//0 not available 1 available

                    dialog.dismiss();
                    Toast.makeText(BookProfile.this, "Added to your Book list", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    newUser = bookDatabaseReference.child(userName);
                    newUser.child("username").setValue(userName);
                    newUser.child("availability").setValue(1);
                    profileDatabaseReference.child(book.getParent()).child("availability").setValue(1);//0 not available 1 available

                    dialog.dismiss();
                    Toast.makeText(BookProfile.this, "Added to your Book list", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.show();
        }

        if(id==R.id.wishListid) {
            addToWishList();
            Toast.makeText(BookProfile.this, "Added to your Wish list", Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.checkAvailability){
            mapWaitDialog.setMessage("Please Wait...");
            mapWaitDialog.show();
            HomePageActivity.profileInfoArrayList.clear();
            for(int i=0;i<usersArrayList.size();i++){
                Query mQuery = FirebaseDatabase.getInstance().getReference().child("Profile").orderByChild("username").equalTo(usersArrayList.get(i).getUsername());
                mQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            ProfileInfo info = postSnapshot.getValue(ProfileInfo.class);
                            HomePageActivity.profileInfoArrayList.add(info);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }

            Intent intent = new Intent(BookProfile.this,MapActivity.class);
            startActivity(intent);
            if(mapWaitDialog.isShowing())
                mapWaitDialog.dismiss();
        }
        if(id==R.id.addToPublicList){
            addToBooklist();
            AlertDialog alertDialog = new AlertDialog.Builder(BookProfile.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Do you want to create a new public booklist or add this to an existing one?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Create new list", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    Intent intent = new Intent(BookProfile.this,CreateNewBooklist.class).putExtra("bookObject",book);
                    startActivity(intent);
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add to existing list", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Toast.makeText(BookProfile.this, "existing list", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookProfile.this,AddToExistingBooklist.class);
                    intent.putExtra("bookObject",book);
                    startActivity(intent);
                }
            });

            alertDialog.show();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Intent intent = null;

        switch(id) {
            case R.id.profileid:
                intent = new Intent(this, Profile.class);
                drawer.closeDrawer(GravityCompat.START);
                intent.putExtra("profileID",LoginActivity.user);
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
        this.startActivity(intent);
        return true;
    }
}
