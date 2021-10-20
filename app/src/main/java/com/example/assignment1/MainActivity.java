package com.example.assignment1;

import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.AF;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.AR;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.BE;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.BN;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.CA;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.CS;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.CY;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.EN;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.HI;
import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage.UR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private Spinner fromSpinner,toSpinner;
    private TextInputEditText sourceEdt;
    MaterialButton translateBtn;
    TextView translatedTV;
    String[] fromLangauges = {"From","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
    String[] toLangauges = {"To","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};

    private static final int REQUEST_PERMISSION_CODE =1;
    int langaugeCode,fromLangaugeCode,toLangaugeCode =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEditSource);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedTV =findViewById(R.id.idTVtranslatedTV);


        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLangaugeCode = getLanguageCode(fromLangauges[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter  = new ArrayAdapter(this,R.layout.spinner_item,fromLangauges);
        fromAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLangaugeCode = getLanguageCode(toLangauges[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter  = new ArrayAdapter(this,R.layout.spinner_item,toLangauges);
        toAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateBtn.setText("");
                if(sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your text to translate",Toast.LENGTH_SHORT).show();
                }
                else if(fromLangaugeCode==0){
                    Toast.makeText(MainActivity.this,"Please select source language",Toast.LENGTH_SHORT).show();
                }
                else if(toLangaugeCode==0){
                    Toast.makeText(MainActivity.this,"Please select the language to make translation",Toast.LENGTH_SHORT).show();
                }
                else{
                    translateText(fromLangaugeCode,toLangaugeCode,sourceEdt.getText().toString());
                    translateBtn.setText("Translate");

                }
            }
        });
    }

    private void translateText(int fromLangaugeCode, int toLangaugeCode, String source) {

        translatedTV.setText("Dowloading Modal..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLangaugeCode)
                .setTargetLanguage(toLangaugeCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translatedTV.setText("Translating..");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(MainActivity.this,"Fail to translate :" + e.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Fail to dowload langauge model",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private int getLanguageCode(String Langauge) {
        int languageCode = 0;
        switch (Langauge){
            case "English":
                languageCode = EN;
                break;
            case "Afrikaans":
                languageCode = AF;
                break;
            case "Arabic":
                languageCode = AR;
                break;
            case "Belarusian":
                languageCode = BE;
                break;
            case "Bengali":
                languageCode = BN;
                break;
            case "Catalan":
                languageCode = CA;
                break;
            case "Czech":
                languageCode = CS;
                break;
            case "Welsh":
                languageCode = CY;
                break;
            case "Hindi":
                languageCode = HI;
                break;
            case "Urdu":
                languageCode = UR;
                break;
            default:
                languageCode=0;

        }
        return languageCode;

    }
}