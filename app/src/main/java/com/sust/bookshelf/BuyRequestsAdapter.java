package com.sust.bookshelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BuyRequestsAdapter extends RecyclerView.Adapter<BuyRequestsAdapter.BuyRequestsViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(BuyRequest buyRequest);
    }
    Context context;
    ArrayList<BuyRequest> arrayList;
    OnItemClickListener listener;
    FirebaseDatabase database;

    public BuyRequestsAdapter(Context context,ArrayList<BuyRequest> arrayList,
                              OnItemClickListener listener,FirebaseDatabase database){
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.database = database;

    }
    @NonNull
    @Override
    public BuyRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.buyrequestrecyclerview,viewGroup,false);
        return new BuyRequestsAdapter.BuyRequestsViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyRequestsViewHolder buyRequestsViewHolder, int i) {
        buyRequestsViewHolder.setDetails(arrayList.get(i));
        buyRequestsViewHolder.bind(arrayList.get(i),listener);
        buyRequestsViewHolder.setVisibility(arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BuyRequestsViewHolder extends RecyclerView.ViewHolder {
        TextView title,username;
        public BuyRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.buybookid);
            username = itemView.findViewById(R.id.buyrequestfrom);
        }

        public void setDetails(BuyRequest buyRequest){
            title.setText(buyRequest.getBoookTitle());
            username.setText(buyRequest.getUsername());
        }

        public void setVisibility(BuyRequest buyRequest){
            if(buyRequest.getStatus()==5){
                itemView.findViewById(R.id.confirmBtn).setVisibility(View.VISIBLE);
            }
            if(buyRequest.getStatus()==6){
                itemView.findViewById(R.id.cancelBtn).setVisibility(View.GONE);
                itemView.findViewById(R.id.confirmBtn).setVisibility(View.GONE);
            }
        }

        public void bind(BuyRequest buyRequest,OnItemClickListener listener){

        }
    }
}
