package com.namsweatech.translate.onBoarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.namsweatech.translate.MainActivity;
import com.namsweatech.translate.R;
import com.namsweatech.translate.Welcome;

import java.util.ArrayList;
import java.util.List;

public class OnOboardActivity extends AppCompatActivity {

    private OnBoardingAdapter onBoardingAdapter;
    private LinearLayout layoutOnBoardingIndicators;
    private MaterialButton buttononBoardAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_oboard);
        layoutOnBoardingIndicators = findViewById(R.id.layoutOnBoardIndicators);
        buttononBoardAction = findViewById(R.id.buttonOnBoardAction);

        setupOnboardingItems();

        ViewPager2 onBoardPager = findViewById(R.id.onBoardViewPager);
        onBoardPager.setAdapter(onBoardingAdapter);

        setupBoardingIndicators();
        setCurrentIndicator(0);

        onBoardPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        buttononBoardAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBoardPager.getCurrentItem() +1 < onBoardingAdapter.getItemCount()){
                    onBoardPager.setCurrentItem(onBoardPager.getCurrentItem()+1);
                } else {
                         startActivity(new Intent(getApplicationContext(), Welcome.class));
                          finish();
                }
            }
        });
    }

    private void setupOnboardingItems(){
        List<OnBoardingItem> onBoardingItems = new ArrayList<>();

        OnBoardingItem itemChooseLang = new OnBoardingItem();
        itemChooseLang.setTitle("Choose language from the dropdown menu" +
                ", from language and to language, then write the word you want to be translated click translate to be translated ");

        OnBoardingItem itemWriteAword = new OnBoardingItem();
        itemWriteAword.setTitle("Here you will write your word to be translated");


        OnBoardingItem itemAdd = new OnBoardingItem();
        itemAdd.setTitle("You can add a word from Top right corner");

        onBoardingItems.add(itemChooseLang);
        onBoardingItems.add(itemWriteAword);
        onBoardingItems.add(itemAdd);

        onBoardingAdapter = new OnBoardingAdapter(onBoardingItems);
    }
    private void setupBoardingIndicators(){
        ImageView[] indicators = new ImageView[onBoardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(8,0,8,0);
        for(int i=0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnBoardingIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index){
        int childCount = layoutOnBoardingIndicators.getChildCount();
        for(int i=0; i<childCount; i++){
            ImageView imageView = (ImageView) layoutOnBoardingIndicators.getChildAt(i);
            if(i== index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if(index == onBoardingAdapter.getItemCount()-1){
            buttononBoardAction.setText("Start");
        } else {
            buttononBoardAction.setText("Next");
        }
    }
}