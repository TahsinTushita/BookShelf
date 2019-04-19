package com.sust.bookshelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        private Button reqBtn;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ownerName);
            reqBtn = itemView.findViewById(R.id.reqBtn);
        }

        public void setDetails(ProfileInfo profileInfo) {
            textView.setText(profileInfo.getUsername());
        }

        public void bind(ProfileInfo profileInfo,OnClickListener listener) {

        }

    }
}
