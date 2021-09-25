package com.namsweatech.translate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AddWordActivity extends AppCompatActivity {

    private String[] tribesList = {"Swahili", "English",
            "Bena", "Bondei", "Chaga",
            "Fipa", "Gogo",
            "Ha", "Hadza",
            "Hangaza", "Haya",
            "Hehe", "Iraqw",
            "Jita", "Kabwa",
            "Kaguru", "Kamba",
            "Kara", "Kerewe",
            "Kikuyu", "Kinga",
            "Kuria", "Kutu",
            "Kw’adza", "Kwere",
            "Luguru", "Luo",
            "Maasai", "Machinga",
            "Mbulu", "Makonde",
            "Makua", "Manyema",
            "Manda", "Matengo",
            "Matumbi", "Meru",
            "Mwera", "Ndamba", "Ndendeule",
            "Ndengereko", "Ndonde", "Ngindo",
            "Ngoni", "Ngazija",
            "Nyakyusa", "Nyasa",
            "Nyambo", "Nyamwanga",
            "Nyamwezi", "Nyanyembe",
            "Nyaturu", "Nyiramba",
            "Pangwa", "Pare",
            "Pogolo", "Rangi",
            "Safwa", "Sagara", "Sandawe",
            "Shambaa", "Shirazi",
            "Sukuma", "Yao",
            "Zanaki", "Zaramo",
            "Zigua",
    };

    private TextInputEditText wordTv, descriptionsTv;
    private AutoCompleteTextView toTribeTV, fromTribeTv;
    TextView worInTv;
    Button addBtn;
    private DatabaseReference dbReference;
    private String firstLetter, fullWord;

    ProgressDialog dialog;

    private ArrayAdapter<String> fromTribeAdapter, toTribeAdapter;
    String fromTribeString, toTribeString, wordString, descriptionString;

    Toolbar toolbar;
    String lang_one, lang_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Add Word");

        dbReference = FirebaseDatabase.getInstance().getReference();

        toTribeTV = findViewById(R.id.toTribeTv);
        fromTribeTv = findViewById(R.id.fromTribeTv);
        wordTv = findViewById(R.id.wordTv);
        worInTv = findViewById(R.id.wordInTv);
        descriptionsTv = findViewById(R.id.descriptionsTv);
        addBtn = findViewById(R.id.but_add);

        fromTribeAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_to_item, tribesList);
        fromTribeAdapter.setDropDownViewResource(R.layout.dropdown_to_item);
        fromTribeTv.setAdapter(fromTribeAdapter);

        toTribeAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_to_item, tribesList);
        toTribeAdapter.setDropDownViewResource(R.layout.dropdown_to_item);
        toTribeTV.setAdapter(toTribeAdapter);

        dialog = new ProgressDialog(this);

        fromTribeTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                lang_one = selectedItem;
                worInTv.setText("Word in "+selectedItem);
                TextInputLayout fromTribe = findViewById(R.id.fromTribeHead);
                fromTribe.setHint("");
            }
        });

        toTribeTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                descriptionsTv.setHint("Enter description in "+selectedItem);
                lang_two =  selectedItem;
                TextInputLayout toTribe = findViewById(R.id.toTribeHead);
                                toTribe.setHint("");
                                if(selectedItem == lang_one ) {
                                    Toast.makeText(AddWordActivity.this, "Can not translate same language to same language", Toast.LENGTH_SHORT).show();
                                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordString = wordTv.getText().toString();
                toTribeString = toTribeTV.getText().toString();
                fromTribeString = fromTribeTv.getText().toString();
                descriptionString = descriptionsTv.getText().toString();

                if(lang_one==lang_two){
                    Toast.makeText(AddWordActivity.this, "Please choose from and to languages", Toast.LENGTH_SHORT).show();
                     return;
                }

                if (!wordString.isEmpty() & !toTribeString.isEmpty() & !fromTribeString.isEmpty() & !descriptionString.isEmpty()) {
                    firstLetter = wordTv.getText().toString().substring(0, 1);
                    fullWord = wordTv.getText().toString();
                    addWord(fromTribeString, toTribeString, wordString, descriptionString);
                } else {
                    Toast.makeText(AddWordActivity.this, "Fill all", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void addWord(String fromLan, String toLan, String word, String descr) {
        dialog.setTitle("Adding  \"" + word + " \"");
        dialog.setMessage("Please wait...");
        dialog.show();
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("fromTribe", fromLan.toLowerCase());
        hashMap.put("toTribe", toLan.toLowerCase());
        hashMap.put("word", word);
        hashMap.put("description", descr);

        dbReference.child("words").child(fromLan).child(toLan).child(firstLetter.toUpperCase()).child(fullWord.toLowerCase()).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added
                        Toast.makeText(AddWordActivity.this, "'" + wordTv.getText() + "' Added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AddWordActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}