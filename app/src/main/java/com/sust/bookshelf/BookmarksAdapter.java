package com.sust.bookshelf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.BookmarksViewHolder> {
    Context context;
    ArrayList<BookMarks> arrayList;

    public BookmarksAdapter(Context context,ArrayList<BookMarks> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout layout= (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmarks_recyclerview,viewGroup,false);
        return new BookmarksAdapter.BookmarksViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksViewHolder bookmarksViewHolder, int i) {

        BookMarks bookMarks=arrayList.get(i);
        bookmarksViewHolder.setDetails(bookMarks);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BookmarksViewHolder extends RecyclerView.ViewHolder {
        ImageView imgurl;
        TextView title,author,pageno;
        public BookmarksViewHolder(@NonNull View itemView) {
            super(itemView);
            imgurl=itemView.findViewById(R.id.bookCover);
            title=itemView.findViewById(R.id.bookTitle);
            author=itemView.findViewById(R.id.bookAuthor);
            pageno=itemView.findViewById(R.id.pageno);
        }

        public void setDetails(BookMarks bookMarks){
            title.setText(bookMarks.getTitle());
            author.setText(bookMarks.getAuthor());
            pageno.setText(bookMarks.getPageno());
            Picasso.get().load(bookMarks.getImgurl()).into(imgurl);
        }
    }
}
