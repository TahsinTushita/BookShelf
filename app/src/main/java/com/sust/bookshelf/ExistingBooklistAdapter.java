package com.sust.bookshelf;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExistingBooklistAdapter extends RecyclerView.Adapter<ExistingBooklistAdapter.ExistingBooklistViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ExistingBooklist existingBooklist);
    }

    ArrayList<ExistingBooklist> existingBooklistArrayList;
    Context context;
    OnItemClickListener listener;

    public ExistingBooklistAdapter(Context context,ArrayList<ExistingBooklist> existingBooklistArrayList,OnItemClickListener listener){
        this.context = context;
        this.existingBooklistArrayList = existingBooklistArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExistingBooklistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.exisitng_booklist_recyclerview,viewGroup,false);
        return new ExistingBooklistAdapter.ExistingBooklistViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ExistingBooklistViewHolder existingBooklistViewHolder, int i) {
        ExistingBooklist existingBooklist = existingBooklistArrayList.get(i);
        existingBooklistViewHolder.setDetils(existingBooklist);
        existingBooklistViewHolder.bind(existingBooklistArrayList.get(i),listener);
    }

    @Override
    public int getItemCount() {
        return existingBooklistArrayList.size();
    }

    public class ExistingBooklistViewHolder extends RecyclerView.ViewHolder {
        TextView listname;

        public ExistingBooklistViewHolder(@NonNull View itemView) {
            super(itemView);
            listname = itemView.findViewById(R.id.listName);
        }

        public void setDetils(ExistingBooklist existingBooklist){
            listname.setText(existingBooklist.getListname());
        }

        public void bind(final ExistingBooklist existingBooklist, final OnItemClickListener listener){

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(existingBooklist);
                }
            });
        }
    }
}
