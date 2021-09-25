package com.namsweatech.translate.onBoarding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.namsweatech.translate.R;

import java.util.List;

public class OnBoardingAdapter extends RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder> {

    private List<OnBoardingItem> onBoardingItemList;

    public OnBoardingAdapter(List<OnBoardingItem> onBoardingItemList) {
        this.onBoardingItemList = onBoardingItemList;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
                 holder.setOnboardingData(onBoardingItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return onBoardingItemList.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textDescr;
        ImageView imageOnBoard;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.titleTv);
        }

        void  setOnboardingData(OnBoardingItem onBoardingItem){
            textTitle.setText(onBoardingItem.getTitle());
        }

    }
}