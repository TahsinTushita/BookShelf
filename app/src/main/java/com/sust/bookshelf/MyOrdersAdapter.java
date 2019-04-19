package com.sust.bookshelf;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> {



    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersViewHolder myOrdersViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        public MyOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
