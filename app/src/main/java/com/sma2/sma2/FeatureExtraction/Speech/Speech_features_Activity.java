package com.sma2.sma2.FeatureExtraction.Speech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities.ArtFeatures_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities.PhonFeatures_Activity;
import com.sma2.sma2.FeatureExtraction.Speech.SpeechFeatures_Activities.ProsFeatures_Activity;
import com.sma2.sma2.MainActivityMenu;
import com.sma2.sma2.R;

import java.util.ArrayList;
import java.util.List;

public class Speech_features_Activity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton  bProsFt, bPhonFt, bArtFt;
    private Button bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_features);
        bBack = findViewById(R.id.button_back_speech);
        bProsFt = findViewById(R.id.button_ProsFt);
        bPhonFt = findViewById(R.id.button_PhonFt);
        bArtFt = findViewById(R.id.button_ArtFt);


        SetListeners();


    }


    private void SetListeners() {
        bBack.setOnClickListener(this);
        bProsFt.setOnClickListener(this);
        bPhonFt.setOnClickListener(this);
        bArtFt.setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_back_speech:
                onButtonBack();
                break;
            case R.id.button_ProsFt:
                onButtonProsFT();
                break;
            case R.id.button_PhonFt:
                onButtonPhonFT();
                break;
            case R.id.button_ArtFt:
                onButtonArtFT();
                break;

        }
    }

    private void onButtonBack() {
        Intent i = new Intent(Speech_features_Activity.this, MainActivityMenu.class);
        startActivity(i);

    }


    private void onButtonProsFT() {
        Intent i = new Intent(Speech_features_Activity.this, ProsFeatures_Activity.class);
        startActivity(i);

    }

    private void onButtonPhonFT() {
        Intent i = new Intent(Speech_features_Activity.this, PhonFeatures_Activity.class);
        startActivity(i);

    }

    private void onButtonArtFT() {
        Intent i = new Intent(Speech_features_Activity.this, ArtFeatures_Activity.class);
        startActivity(i);

    }

}