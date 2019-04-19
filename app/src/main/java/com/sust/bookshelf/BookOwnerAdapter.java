package com.sust.bookshelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class BookOwnerAdapter extends RecyclerView.Adapter<BookOwnerAdapter.BookViewHolder> {

    interface OnClickListener {
        void onClick();
    }

    private Context context;
    private ArrayList<ProfileInfo> bookOwners;
    private OnClickListener listener;

    public BookOwnerAdapter(Context context, ArrayList<ProfileInfo> bookOwners, OnClickListener listener) {
        this.context = context;
        this.bookOwners = bookOwners;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.book_owner_list,viewGroup,false);
        return new  BookViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder bookViewHolder, int i) {
        ProfileInfo profileInfo = bookOwners.get(i);
        bookViewHolder.setDetails(profileInfo);
        bookViewHolder.bind(profileInfo,listener);
    }

    @Override
    public int getItemCount() {
        return bookOwners.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.ownerName);
        }

        public void setDetails(final ProfileInfo profileInfo) {
        Query query = FirebaseDatabase.getInstance().getReference("Profile").orderByChild("username").equalTo(profileInfo.getUsername());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    ProfileInfo nprofile = dataSnapshot.getValue(ProfileInfo.class);
                    if (profileInfo.getAvailability() == 0 || nprofile.getShareaddress() == 0) {

                        imageView.setVisibility(View.GONE);

                    }
                    if(nprofile.getName() != null )
                        textView.setText(nprofile.getName());
                    else textView.setText(nprofile.getUsername());
                } catch (Exception e) {
                    System.out.println(dataSnapshot.toString());
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

        public void bind(ProfileInfo profileInfo,OnClickListener listener) {

        }

    }
}
