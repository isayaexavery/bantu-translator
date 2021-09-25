package com.namsweatech.translate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.viewHolder> {

    private Context context;
    private ArrayList<WordsModel> mCategoryList;

    public WordsAdapter(ArrayList<WordsModel> mCategoryList, Context context) {
        this.context = context;
        this.mCategoryList = mCategoryList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_words, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        holder.wordsTv.setText(mCategoryList.get(position).getWord());
        holder.descriptionTv.setText(mCategoryList.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cliked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView wordsTv, descriptionTv;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            wordsTv = itemView.findViewById(R.id.wordTv);
            descriptionTv = itemView.findViewById(R.id.descriptionsTv);
        }
    }
}
