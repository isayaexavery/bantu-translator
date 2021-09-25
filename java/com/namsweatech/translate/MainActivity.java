package com.namsweatech.translate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String[] tribesList = {"Swahili", "English",
            "Bena", "Bondei", "Chaga", "Fipa", "Gogo",
            "Ha", "Hadza", "Hangaza", "Haya", "Hehe",
            "Iraqw", "Jita", "Kabwa", "Kaguru", "Kamba",
            "Kara", "Kerewe", "Kikuyu", "Kinga", "Kuria",
            "Kutu", "Kw’adza", "Kwere", "Luguru", "Luo",
            "Maasai", "Machinga", "Mbulu", "Makonde",
            "Makua", "Manyema", "Manda", "Matengo",
            "Matumbi", "Meru", "Mwera", "Ndamba", "Ndendeule",
            "Ndengereko", "Ndonde", "Ngindo", "Ngoni", "Ngazija",
            "Nyakyusa", "Nyasa", "Nyambo", "Nyamwanga",
            "Nyamwezi", "Nyanyembe", "Nyaturu", "Nyiramba",
            "Pangwa", "Pare", "Pogolo", "Rangi",
            "Safwa", "Sagara", "Sandawe", "Shambaa", "Shirazi",
            "Sukuma", "Yao", "Zanaki", "Zaramo", "Zigua",
    };

    public static DatabaseReference databaseReference;
    Toolbar toolbar;
    private WordsAdapter wordsAdapter;

    LinearLayout preparingL;
    AutoCompleteTextView fromLangTv, toLangTv;
    Button butArrow, butTranslate, butClear;
    ProgressBar progressBar;
    EditText etInput;
    RecyclerView recyclerView;

    private ArrayAdapter<String> fromDropDownAdapter, toDropDownAdapter;
    private ArrayList<String> fromLangList, toLangList;

    private ArrayList<WordsModel> wordsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        //initialize views
        initViews();
        //load all languages from--> to
        loadFromSpinner();
        loadToSpinner();
    }

    private void initViews() {
        preparingL = findViewById(R.id.preparingLayout);
        preparingL.setVisibility(View.VISIBLE);
        fromLangTv = findViewById(R.id.et_from_lang);
        toLangTv = findViewById(R.id.et_to_lang);
        etInput = (EditText) findViewById(R.id.et_input_text);
        butTranslate = (Button) findViewById(R.id.but_translate);
        butClear = (Button) findViewById(R.id.but_clear);
        butArrow = (Button) findViewById(R.id.but_arrow);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.output_recycler);
        wordsModelArrayList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        wordsAdapter = new WordsAdapter(wordsModelArrayList, getApplicationContext());
        recyclerView.setAdapter(wordsAdapter);

        //below code to checks behaviors of the input text
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String etInputString = s.toString();
                String fromLanguage = fromLangTv.getText().toString().trim();
                String toLanguage = toLangTv.getText().toString().trim();

                if (s.toString().isEmpty()) {
                    //clear all previsous results after deleting input
                    wordsModelArrayList.clear();
                    wordsAdapter.notifyDataSetChanged();
                    //show keyboard
                   // openKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        butTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etInputString = etInput.getText().toString();
                String fromLanguage = fromLangTv.getText().toString().trim();
                String toLanguage = toLangTv.getText().toString().trim();

                if(fromLanguage==toLanguage){
                    Toast.makeText(MainActivity.this, "Can not translate same language to same language", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!etInputString.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    TranslateWord(etInputString, fromLanguage, toLanguage);
                } else {
                    Toast.makeText(MainActivity.this, "Enter a word", Toast.LENGTH_SHORT).show();
                }
            }
        });

        butClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear textInput
                etInput.setText("");
                //clear list and notfy the adapter
                wordsModelArrayList.clear();
                wordsAdapter.notifyDataSetChanged();
                openKeyboard();
            }
        });

        //toggle the from to language
        butArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "To be implemented later", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void TranslateWord(String word, String fromLan, String toLan) {
//below line checks the from database with reference 'words'
        //then input is being cut to a single first  letter
        Query query = databaseReference.child("words").child(fromLan).child(toLan).child(etInput.getText().toString().toUpperCase().substring(0, 1))
                .orderByChild("word")
                .startAt(word.toLowerCase()).endAt(word.toLowerCase() + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                wordsModelArrayList.clear();

                if (!snapshot.exists()) {
                    Toast.makeText(MainActivity.this, "Does not exists", Toast.LENGTH_SHORT).show();
                    //openKeyboard();
                }
                for (DataSnapshot ds : snapshot.getChildren()) {

                    WordsModel wordsModel = ds.getValue(WordsModel.class);
                    wordsModelArrayList.add(wordsModel);
                }

                wordsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                //closeKeyboard();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    //Create and inflate(show) menu items
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_translate, menu);
        return true;
    }

    //Handle menu options click events
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_word:
                Intent startAddWord = new Intent(this, AddWordActivity.class);
                startActivity(startAddWord);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //To load languages data from database to be checked later
    private void getFromLanguages() {

        fromLangList = new ArrayList<>();

        // fromDropDownAdapter.setDropDownViewResource(R.layout.dropdown_from_item);
        // fromLangTv.setAdapter(fromDropDownAdapter);
        databaseReference.child("words").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // WordsModel model = new WordsModel();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //To continue here
                    fromLangList.add(snapshot1.getKey());
                }
                //fromDropDownAdapter.notifyDataSetChanged();
                loadFromSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //load all languages fron
    private void loadFromSpinner() {
        fromDropDownAdapter = new ArrayAdapter<>(this, R.layout.dropdown_from_item, tribesList);
        fromLangTv.setAdapter(fromDropDownAdapter);

        //Just mbwe mbwe of visibility gone
        Transition transition = new Fade();
        transition.setDuration(800);
        transition.addTarget(R.id.preparingLayout);

        TransitionManager.beginDelayedTransition(preparingL, transition);
        preparingL.setVisibility(View.GONE);

    }

    //To load languages data from database to be checked later
    private void getToLanguages(String toLangString) {

        toLangList = new ArrayList<>();
        databaseReference.child("words").child(toLangString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toLangList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    //To continue here
                    toLangList.add(snapshot1.getKey());
                }
                loadToSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //load list of la
    private void loadToSpinner() {

        toDropDownAdapter = new ArrayAdapter<>(this, R.layout.dropdown_from_item, tribesList);
        toLangTv.setAdapter(toDropDownAdapter);
    }

    private void closeKeyboard() {
        // now assign the system
        // service to InputMethodManager
        InputMethodManager manager
                = (InputMethodManager)
                getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        manager
                .hideSoftInputFromWindow(
                        etInput.getWindowToken(), 0);
    }


    private void openKeyboard() {
        // now assign the system
        // service to InputMethodManager
        InputMethodManager manager
                = (InputMethodManager)
                getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        manager
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

}